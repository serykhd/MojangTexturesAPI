package ru.serykhd.mojang;

import ru.serykhd.mojang.exception.MojangProfileNotFoundException;
import ru.serykhd.mojang.profile.IProfileProperties;
import ru.serykhd.mojang.profile.IProfileUUID;
import ru.serykhd.mojang.profile.impl.ProfileProperties;
import ru.serykhd.mojang.profile.impl.ProfileUUID;
import ru.serykhd.mojang.utils.NickValidator;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.NonNull;
import ru.serykhd.common.Validator;
import ru.serykhd.common.primitive.BooleanReverse;
import ru.serykhd.common.ratelimiter.impl.RateLimiter;
import ru.serykhd.common.thread.SThreadFactory;
import ru.serykhd.http.HttpClient;
import ru.serykhd.http.callback.HttpCallback;
import ru.serykhd.http.exception.HttpIOException;
import ru.serykhd.http.exception.HttpTooManyRequestsExeption;
import ru.serykhd.http.mime.MimeType;
import ru.serykhd.http.requst.WHttpRequestBuilder;
import ru.serykhd.http.response.WHttpRequstResponse;
import ru.serykhd.http.utils.RequestUtils;
import ru.serykhd.http.utils.RequstResponseUtils;

import java.time.Duration;

public class MojangAPI extends HttpClient {
	
	// https://wiki.vg/Mojang_API
	
	private static final String UUID_URL = "https://api.mojang.com/users/profiles/minecraft/%s";
	private static final String TEXTURES_URL = "https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false";
	private static final String TEXTURES_JOINED_URL = "https://sessionserver.mojang.com/session/minecraft/hasJoined?username=%s&serverId=%s&ip=%s";

	private final RateLimiter apiLimiter = new RateLimiter(600, Duration.ofMinutes(10));
	private final RateLimiter texturesLimiter = new RateLimiter(200, Duration.ofMinutes(1));

	
	public MojangAPI() {
		super(new SThreadFactory("MojangAPI HttpClient Group"));
	}
	
	public void getProfileUUID(@NonNull String name, @NonNull HttpCallback<IProfileUUID> response) {
		Validator.isTrue(NickValidator.isValidName(name, true));

		if (isDirect()) {
			if (BooleanReverse.reverse(apiLimiter.tryAcquire())) {
				throw new HttpTooManyRequestsExeption();
			}
		}

		//
		WHttpRequestBuilder reqBuilder = create(String.format(UUID_URL, name), new HttpCallback<>() {

			@Override
			public void done(WHttpRequstResponse result) {
				if (result.getResponse().status() == HttpResponseStatus.TOO_MANY_REQUESTS) {
					response.cause(new HttpTooManyRequestsExeption());
					return;
				}
				
				if (result.getResponse().status() == HttpResponseStatus.NO_CONTENT) {
					response.cause(new MojangProfileNotFoundException(name));
					return;
				}
				
				if (result.getResponse().status() != HttpResponseStatus.OK) {
					response.cause(new HttpIOException(String.format("bad status code: %s | %s", result.getResponse().status(), result.getContent())));
					return;
				}
				
				response.done(RequestUtils.getGson().fromJson(result.getContent().toString(), ProfileUUID.class));
			}

			@Override
			public void cause(Throwable cause) {
				response.cause(cause);
			}
		});
		
		reqBuilder.acceptType(MimeType.JSON);

		execute(reqBuilder.createRequst());
	}

	public void getProfileTextures(@NonNull IProfileUUID profile, @NonNull HttpCallback<IProfileProperties> response) {
		Validator.isTrue(profile.getId().length() == 32);

		if (isDirect()) {
			if (BooleanReverse.reverse(texturesLimiter.tryAcquire())) {
				throw new HttpTooManyRequestsExeption();
			}
		}

		WHttpRequestBuilder reqBuilder = create(String.format(TEXTURES_URL, profile.getId()), new HttpCallback<>() {

			@Override
			public void done(WHttpRequstResponse result) {
				if (result.getResponse().status() == HttpResponseStatus.TOO_MANY_REQUESTS) {
					response.cause(new HttpTooManyRequestsExeption());
					return;
				}

				if (result.getResponse().status() == HttpResponseStatus.NO_CONTENT) {
					response.cause(new MojangProfileNotFoundException(String.format("id: %s", profile.getId())));
					return;
				}

				if (result.getResponse().status() != HttpResponseStatus.OK) {
					response.cause(new HttpIOException(String.format("bad status code: %s | %s", result.getResponse().status(), result.getContent())));
					return;
				}

				response.done(RequestUtils.getGson().fromJson(result.getContent().toString(), ProfileProperties.class));
			}

			@Override
			public void cause(Throwable cause) {
				response.cause(cause);
			}
		});

		reqBuilder.acceptType(MimeType.JSON);

		execute(reqBuilder.createRequst());
	}

	public void getProfileTextures(@NonNull String username, @NonNull String serverId, @NonNull String ip, @NonNull HttpCallback<IProfileProperties> response) {
		WHttpRequestBuilder reqBuilder = create(String.format(TEXTURES_JOINED_URL, username, serverId, ip), new HttpCallback<>() {

			@Override
			public void done(WHttpRequstResponse result) {
				RequstResponseUtils.checkOk(result, response);

				response.done(RequestUtils.getGson().fromJson(result.getContent().toString(), ProfileProperties.class));
			}

			@Override
			public void cause(Throwable cause) {
				response.cause(cause);
			}
		});

		reqBuilder.acceptType(MimeType.JSON);

		execute(reqBuilder.createRequst());
	}
}

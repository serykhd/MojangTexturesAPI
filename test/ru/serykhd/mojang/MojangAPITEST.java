package ru.serykhd.mojang;

import com.google.gson.GsonBuilder;
import ru.serykhd.mojang.profile.IProfileProperties;
import ru.serykhd.mojang.profile.IProfileUUID;
import ru.serykhd.http.callback.HttpCallback;

public class MojangAPITEST {

	public static void main(String[] args) throws InterruptedException {
		MojangAPI api = new MojangAPI( );


		api.getProfileUUID("4u4y3uj2", new HttpCallback<IProfileUUID>() {

			@Override
			public void done(IProfileUUID result) {
				//System.out.println(result);
				System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(result));

			}

			@Override
			public void cause(Throwable cause) {
				cause.printStackTrace();
			}
		});

		api.getProfileUUID("Notch", new HttpCallback<IProfileUUID>() {

			@Override
			public void done(IProfileUUID result) {
				//System.out.println(result);
				System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(result));

				api.getProfileTextures(result, new HttpCallback<IProfileProperties>() {

					@Override
					public void done(IProfileProperties result) {
					//	System.out.println(result);

						System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(result));
					}

					@Override
					public void cause(Throwable cause) {
						cause.printStackTrace();
					}
				});

			}

			@Override
			public void cause(Throwable cause) {
				cause.printStackTrace();
			}
		});
	}
}

package me.yaraju.dancetomusic.services;

import android.util.Log;

import com.giphy.sdk.core.models.Image;
import com.giphy.sdk.core.models.Media;
import com.giphy.sdk.core.models.enums.MediaType;
import com.giphy.sdk.core.network.api.CompletionHandler;
import com.giphy.sdk.core.network.api.GPHApi;
import com.giphy.sdk.core.network.api.GPHApiClient;
import com.giphy.sdk.core.network.response.MediaResponse;

public class GifService {

    private GPHApi client;

    public GifService() {
        client = new GPHApiClient("kdydifUpMktFk2Mdd41IODIVoyPWKLzX");
    }

    public void fetchRandom(String tag, final OnSuccessHandler onSuccessHandler, final OnFailureHandler onFailureHandler) {
        client.random(tag, MediaType.gif, null, new CompletionHandler<MediaResponse>() {
            @Override
            public void onComplete(MediaResponse result, Throwable throwable) {
                if (result == null) {
                    onFailureHandler.onFail(throwable);
                } else {
                    Media data = result.getData();

                    GifInfo.Builder builder = new GifInfo.Builder();
                    if (data != null) {
                        Log.v("giphy", data.getId());
                        Log.v("giphy", getGifUrl(data));
                        builder.id(data.getId()).url(getGifUrl(data)).tags(data.getTags());
                    } else {
                        Log.e("giphy error", "No results found.");
                    }
                    GifInfo info = builder.build();
                    onSuccessHandler.onSuccess(info);
                }
            }
        });
    }

    private String getGifUrl(Media data) {
        Image gifImage;
        gifImage = data.getImages().getFixedHeight();
        if (gifImage!= null && !gifImage.getGifUrl().isEmpty()) return gifImage.getGifUrl();
        gifImage = data.getImages().getFixedWidth();
        if (gifImage!= null && !gifImage.getGifUrl().isEmpty()) return gifImage.getGifUrl();
        gifImage = data.getImages().getFixedHeightSmall();
        if (gifImage!= null && !gifImage.getGifUrl().isEmpty()) return gifImage.getGifUrl();
        gifImage = data.getImages().getFixedWidthSmall();
        return gifImage.getGifUrl();
    }

    public void fetchById(String id, final OnSuccessHandler onSuccessHandler, final OnFailureHandler onFailureHandler) {
        client.gifById(id, new CompletionHandler<MediaResponse>() {
            @Override
            public void onComplete(MediaResponse mediaResponse, Throwable throwable) {
                Media data = mediaResponse.getData();
                if (data != null) {
                    Log.v("giphy", data.getEmbedUrl());
                    GifInfo.Builder builder = new GifInfo.Builder();
                    builder.id(data.getId()).url(data.getEmbedUrl()).tags(data.getTags());
                    onSuccessHandler.onSuccess(builder.build());
                }
            }
        });
    }

}

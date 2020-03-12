package com.dlanuara.ejercicioc.models.paymentmethod.cardissuer;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class Issuer {
    private @NonNull String id;
    private @NonNull String name;
    private @NonNull String secure_thumbnail;
    private @NonNull String thumbnail;

    public Issuer( @NonNull String id, @NonNull String name, @NonNull String secure_thumbnail,
                   @NonNull String thumbnail){
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
        this.secure_thumbnail = secure_thumbnail;
    }
    public Issuer(@NonNull JSONObject json) throws JSONException {
        this(
                json.getString("id"),
                json.getString("name"),
                json.getString("secure_thumbnail"),
                json.getString("thumbnail")
                );
    }

    public final @NonNull String getId() {
        return id;
    }

    public final @NonNull String getName() {
        return name;
    }

    public final @NonNull String getSecure_thumbnail() {
        return secure_thumbnail;
    }

    public final @NonNull String getThumbnail() {
        return thumbnail;
    }

    @Override
    public String toString(){
        return this.name;
    }
}

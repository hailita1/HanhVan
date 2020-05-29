package com.example.haili.btl.model;


public class Place {
    private String placeId;
    private String categoryId;
    private byte[] image;
    private String name;
    private String address;
    private String description;
    private double lat;
    private double lng;

    public Place(Builder builder) {
        this.placeId = builder.placeId;
        this.image = builder.image;
        this.categoryId = builder.categoryId;
        this.name = builder.name;
        this.address = builder.address;
        this.description = builder.description;
        this.lat = builder.lat;
        this.lng = builder.lng;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public byte[] getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public static class Builder {
        private String placeId;
        private String categoryId;
        private byte[] image;
        private String name;
        private String address;

        public Builder setCategoryId(String categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        private String description;
        private double lat;
        private double lng;

        public Builder setPlaceId(String placeId) {
            this.placeId = placeId;
            return this;
        }

        public Builder setImage(byte[] image) {
            this.image = image;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setLat(double lat) {
            this.lat = lat;
            return this;
        }

        public Builder setLng(double lng) {
            this.lng = lng;
            return this;
        }

        public Place build() {
            return new Place(this);
        }
    }

}


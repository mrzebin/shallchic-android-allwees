package com.hb.basemodel.image;


import androidx.annotation.DrawableRes;

public class ImageConfig {
    final int errorImage;
    final int placeholderImage;
    final int corner;
    final LoadListener listener;
    final int width;
    final int height;

    public ImageConfig(Builder builder) {
        errorImage = builder.errorImage;
        placeholderImage = builder.placeholderImage;
        corner = builder.corner;
        listener = builder.listener;
        width = builder.width;
        height = builder.height;
    }

    public static class Builder {
        private int errorImage;
        private int placeholderImage;
        private int corner;
        private LoadListener listener;
        private int width, height;

        public Builder error(@DrawableRes int error) {
            this.errorImage = error;
            return this;
        }

        public Builder placeholder(@DrawableRes int placeHolder) {
            this.placeholderImage = placeHolder;
            return this;
        }

        public Builder corner(int corner) {
            this.corner = corner;
            return this;
        }

        public Builder listener(LoadListener loadListener) {
            listener = loadListener;
            return this;
        }

        public Builder override(int w, int h) {
            this.width = w;
            this.height = h;
            return this;
        }

        public ImageConfig build() {
            return new ImageConfig(this);
        }
    }
}

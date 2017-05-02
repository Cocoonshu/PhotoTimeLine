package com.cocoonshu.example.phototimeline.data;

import java.util.List;

/**
 * @Author Cocoonshu
 * @Date   2017-04-26
 */
public class MediaSet extends MediaObject {

    public static final int INVALID_COUNT = -1;

    public MediaSet(long version) {
        super(version);
    }

    @Override
    public void reload() {

    }

    @Override
    public void notifyDateChanged() {

    }

    public List<MediaItem> getMediaItem(int start, int count) {
        return null;
    }

    public int getMediaItemCount() {
        return 0;
    }
}

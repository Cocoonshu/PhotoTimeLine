package com.cocoonshu.example.phototimeline.data;


/**
 * @Author Cocoonshu
 * @Date   2017-04-26
 */
public class DateTimeAlbum extends LocalAlbum {

    private static final String COLUMN_CSHOT_ID = "cshot_id";

    public DateTimeAlbum() {
        super(nextVersion());
    }

    /**
     * Update all time information from MediaProvider
     */
    public void updateTimeInfo() {

    }

    /**
     * Get sql where clause sentence
     * @return
     */
    public String getWhereClause() {
        /**
         * SELECT * FROM files
         * WHERE cshot_id == 0 AND
         */
        return "cshot_id";
    }

}

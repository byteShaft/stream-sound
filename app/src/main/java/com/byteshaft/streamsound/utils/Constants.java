package com.byteshaft.streamsound.utils;

public class Constants {
    public interface ACTION {
        String MAIN_ACTION = "com.byteshaft.custom_notification.action.main";
        String INIT_ACTION = "com.byteshaft.custom_notification.action.init";
        String PREV_ACTION = "com.byteshaft.custom_notification.action.prev";
        String PLAY_ACTION = "com.byteshaft.custom_notification.action.play";
        String NEXT_ACTION = "com.byteshaft.custom_notification.action.next";
        String STARTFOREGROUND_ACTION = "com.byteshaft.custom_notification.action.startforeground";
        String STOPFOREGROUND_ACTION = "com.byteshaft.custom_notification.action.stopforeground";

    }

    public interface NOTIFICATION_ID {
        int FOREGROUND_SERVICE = 101;
    }

//     Bitmap getDefaultAlbumArt(Context context) {
//        Bitmap bm = null;
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        try {
//            bm = BitmapFactory.decodeResource(context.getResources(),
//                    R.drawable.default_album_art, options);
//        } catch (Error ee) {
//        } catch (Exception e) {
//        }
//        return bm;
//    }

}

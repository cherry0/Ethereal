package cherry.ethereal.data;

public  class PlayModel {
    private static Integer playModelIndex=1;

    public static Integer getPlayModelIndex() {
        return playModelIndex;
    }

    public static void setPlayModelIndex(Integer playModelIndex) {
        PlayModel.playModelIndex = playModelIndex;
    }
}

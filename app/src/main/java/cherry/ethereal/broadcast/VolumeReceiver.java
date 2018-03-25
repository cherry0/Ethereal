package cherry.ethereal.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.SeekBar;

/**
 * Created by pengbo on 2018/3/24.
 */

public class VolumeReceiver extends BroadcastReceiver{

    private AudioManager AM;
    private SeekBar seekBar;
    public VolumeReceiver(AudioManager _am, SeekBar _seekBar){
        this.AM=_am;
        this.seekBar=_seekBar;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")){
            int currentVolume = AM.getStreamVolume(AudioManager.STREAM_MUSIC);
            seekBar.setProgress(currentVolume);
        }
    }
}

package com.example.squishthebugs;

        import android.content.Context;
        import android.media.AudioAttributes;
        import android.media.AudioManager;
        import android.media.SoundPool;
        import android.os.Build;

class SoundPlayer {
    private static SoundPool soundPool;
    private static int hitSound,gameOverSound,winningSound,failedSound;

    SoundPlayer(Context context)
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // SoundPool is deprecated in API level 21.(Lollipop)
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(3)
                    .build();
        } else {
            soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        }

        hitSound = soundPool.load(context, R.raw.squish_the_bug, 1);
        gameOverSound=soundPool.load(context,R.raw.game_over_sound,1);
        winningSound=soundPool.load(context,R.raw.winning_sound,1);
        failedSound=soundPool.load(context,R.raw.squish_bee,1);

    }
    void playHitSound() {
        soundPool.play(hitSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
    void playGameOverSound() {
        soundPool.play(gameOverSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
    void playWinningSound() {
        soundPool.play(winningSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
    void playFailedSound() {
        soundPool.play(failedSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
}
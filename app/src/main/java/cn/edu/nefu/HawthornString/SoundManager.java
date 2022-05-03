package cn.edu.nefu.HawthornString;

import android.content.Intent;

class SoundManager {
   public static void PlayMergeSound() {
      Intent intent = new Intent(MainActivity.Instance, SoundService.class);
      intent.putExtra("music", R.raw.merge);
      MainActivity.Instance.startService(intent);
   }

   public static void PlayPickupSound() {
      Intent intent = new Intent(MainActivity.Instance, SoundService.class);
      intent.putExtra("music", R.raw.pickup);
      MainActivity.Instance.startService(intent);
   }
}

package cn.edu.nefu.HawthornString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class GameManager {
   private static final int STRING_CNT = 6;
   private volatile static GameManager instance;
   private List<HawthornItem>[] strings = new ArrayList[STRING_CNT];

   private GameManager (){}

   /**
    * @return GameManager单例
    */
   public static GameManager getSingleton() {
      if (instance == null) {
         synchronized (GameManager.class) {
            if (instance == null) {
               instance = new GameManager();
               instance.Init();
            }
         }
      }
      return instance;
   }

   /**
    * 初始化
    */
   private void Init(){
      for(int i=0;i<STRING_CNT;++i){
         // TODO: 这个山楂要初始化一下
         strings[i].add(new HawthornItem());
      }
   }

   /**
    * 拿起山楂
    * @param col 签子序号
    * @return 被拿起的山楂对象，如果没有可拿起的山楂则返回 null
    */
   public List<HawthornItem> pickItems(int col) throws Exception {
      if(col<0 || col>5){
         System.out.println("访问签子时下标越界");
         throw new Exception("下标越界");
      }

      // 签子为空
      if(strings[col].size()==0){
         return null;
      }

      List<HawthornItem> ret = new ArrayList<HawthornItem>();
      ret.add(strings[col].get(strings[col].size()-1));
      strings[col].remove(strings[col].size()-1);

      for(int i=strings[col].size()-1; i>=0; --i){
         if(strings[col].get(i)==ret.get(ret.size()-1)){
            ret.add(strings[col].get(i));
            strings[col].remove(i);
         }
      }
      return ret;
   }

   /**
    * 移动山楂
    * @param from 起始签子
    * @param to 结束签子
    */
   public void moveItems(int from, int to) {

   }

}

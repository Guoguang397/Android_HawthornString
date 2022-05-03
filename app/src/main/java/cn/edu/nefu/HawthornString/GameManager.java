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
      List<Integer> ints = Arrays.asList(1,1,1,1,1,1);
      List<HawthornItem> items = UIManager.GetInstance().createItems(ints);
      for(int i=0;i<STRING_CNT;++i){
         strings[i].add(items.get(i));
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
      List<HawthornItem> str = strings[col];
      ret.add(str.get(strings[col].size()-1));
      str.remove(strings[col].size()-1);

      for(int i=str.size()-1; i>=0; --i){
         if(str.get(i)==ret.get(ret.size()-1)){
            ret.add(str.get(i));
            str.remove(i);
         }
         else break;
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

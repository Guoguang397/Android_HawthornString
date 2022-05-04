package cn.edu.nefu.HawthornString;

import static java.lang.StrictMath.abs;
import static java.lang.StrictMath.max;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class GameManager {
   public int score = 0;

   private static final int STRING_CNT = 6;
   private static final int HW_MAX = 7;
   private static final int MERGE_CNT = 3;
   private volatile static GameManager instance;
   private List<HawthornItem>[] strings = new ArrayList[STRING_CNT];
   private int actCnt = 0;
   private int maxLevel = 1;
   private List<HawthornItem> newHT = new ArrayList<>();

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
    * 选择一根签子
    * @param col 签子序号
    * @return 被拿起的山楂对象，如果没有可拿起的山楂则返回 null
    */
   public List<HawthornItem> pickItems(int col) throws Exception {
      if(col<0 || col>5){
         System.out.println("访问签子时下标越界");
         throw new Exception("访问签子时下标越界");
      }

      return choose(col);
   }

   /**
    * 移动山楂
    * @param from 起始签子
    * @param to 结束签子
    */
   public void moveItems(int from, int to) throws Exception {
      if(from<0 || from>5 || to<0 || to>5){
         System.out.println("访问签子时下标越界");
         throw new Exception("访问签子时下标越界");
      }

      List<HawthornItem> fromStr=strings[from];
      List<HawthornItem> toStr=strings[to];
      if(fromStr.size()==0){
         System.out.println("起始签子为空");
         throw new Exception("起始签子为空");
      }

      List<HawthornItem> picked=choose(from);

      // 删除原签子上的山楂
      for(int i=0;i<picked.size();++i){
         fromStr.remove(fromStr.size()-1);
      }
      // 添加到目标签子上
      UIManager.GetInstance().moveItems(picked, to, toStr.size());
      toStr.addAll(picked);
      // 合并
      merge(to);

      if(toStr.size()> HW_MAX){
         // TODO: Game Over
      }

      ++actCnt;
      // 第三次操作，生成并预览下一轮山楂
      if(actCnt%4==3){
         newHT = UIManager.GetInstance().createItems(generateNewHawthornItemLevel());
         UIManager.GetInstance().previewItems(newHT);
      }
      // 第四次操作，掉落下一轮山楂
      if(actCnt%4==0){
         boolean fail = false;
         List<Integer> dropPos=new ArrayList<>();
         for(int i=0;i<STRING_CNT;++i){
            if(strings[i].size()>=HW_MAX){
               fail=true;
            }
            strings[i].add(newHT.get(i));
            dropPos.add(strings[i].size()-1);
         }
         UIManager.GetInstance().dropItems(newHT,dropPos);
         for(int i=0;i<STRING_CNT;++i){
            merge(i);
         }
         if(fail){
            // TODO: Game Over
         }
      }
   }

   /**
    * 初始化所有签子
    */
   private void Init(){
      strings = new ArrayList[STRING_CNT];
      List<Integer> ints = Arrays.asList(1,1,1,1,1,1);
      List<HawthornItem> items = UIManager.GetInstance().createItems(ints);
      for(int i=0;i<STRING_CNT;++i){
         strings[i] = new ArrayList<HawthornItem>();
         strings[i].add(items.get(i));
      }
      UIManager.GetInstance().showItems(items);
   }

   /**
    * 在这根签字上寻找可被拿起的山楂
    * @param col 签子序号
    * @return 可以被拿起的山楂对象，如果没有可拿起的山楂则返回 null
    */
   private List<HawthornItem> choose(int col){
      List<HawthornItem> str = strings[col];

      // 签子为空
      if(str.size()==0){
         return null;
      }

      List<HawthornItem> ret = new ArrayList<>();
      ret.add(str.get(str.size()-1));

      for(int i=str.size()-2; i>=0; --i){
         if(str.get(i).level == ret.get(ret.size()-1).level){
            ret.add(str.get(i));
         }
         else{
            break;
         }
      }
      return ret;
   }

   /**
    * 递归合并目标签字上可合并的山楂
    * @param col 进行合并的签子
    */
   private void merge(int col){
      List<HawthornItem> same = choose(col);
      if(same.size()<MERGE_CNT){
         return;
      }

      score+=same.get(0).level*same.size();
      UIManager.GetInstance().UpdateScore(score);

      List<HawthornItem> str=strings[col];
      // 删除多余的山楂
      for(int i=0;i<same.size()-1;++i){
         UIManager.GetInstance().removeItem(str.get(str.size()-1));
         str.remove(str.size()-1);
      }

      // 更新最后一个山楂
      HawthornItem newHT=str.get(str.size()-1);
      if(newHT.level>=8){
         UIManager.GetInstance().removeItem(str.get(str.size()-1));
         str.remove(str.size()-1);
      }
      else{
         ++newHT.level;
         maxLevel=max(maxLevel,newHT.level);
         UIManager.GetInstance().changeItem(newHT,newHT.level);
      }
      SoundManager.PlayMergeSound();
      merge(col);
   }

   /**
    * 生成 6 个新山楂
    * @return 生成的山楂
    */
   private List<Integer> generateNewHawthornItemLevel(){
      List<Integer> ret=new ArrayList<>();
      java.util.Random random = new java.util.Random();

      double range=1.5/maxLevel;
      for(int i=0;i<STRING_CNT;++i){
         double randNum= Math.abs(random.nextGaussian());
         int lev=(int)(randNum/range)+1;
         if(lev>maxLevel){
            lev=maxLevel;
         }
         ret.add(lev);
      }

      return ret;
   }
}

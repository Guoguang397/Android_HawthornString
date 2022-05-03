package cn.edu.nefu.HawthornString;

import java.util.ArrayList;
import java.util.List;

class UIManager {

   static UIManager Instance;
   private UIManager(){}

   public static UIManager GetInstance() {
      if(Instance == null) {
         Instance = new UIManager();
      }
      return Instance;
   }

   /** 移动山楂
    * @param hawthornItems 要移动的山楂
    * @param col 移动到第几串上
    * @param row 最下方的果移动到几号位上
    */
   public void moveItems(List<HawthornItem> hawthornItems, int col, int row) {

   }

   /** 更改山楂等级
    * @param hawthornItem 山楂对象
    * @param to 更改后的等级
    */
   public void changeItem(HawthornItem hawthornItem, int to) {

   }

   /** 创建山楂
    * @param hawthornLevels 随机结果
    * @return 返回山楂对象
    */
   public List<HawthornItem> createItems(List<Integer> hawthornLevels) {

      return null;
   }

   /** 预览山楂
    * @param hawthornLevels 山楂对象
    */
   public void previewItems(List<HawthornItem> hawthornLevels) {

   }

   /** 扔下预览的山楂
    * @param hawthornItems 山楂对象
    * @param rowPosition 每根签子山楂落下的位置
    */
   public void dropItems(List<HawthornItem> hawthornItems, List<Integer> rowPosition) {

   }
}

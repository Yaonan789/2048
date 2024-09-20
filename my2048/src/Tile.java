//方块类，包含方块的数值及合并状态
public class Tile{
    private boolean merged;
    private int value;

    Tile(int val){
        value = val;
    }

    int getValue(){
        return value;
    }

    void setMerged(boolean m){
        merged=m;
    }

    //判断两个方块是否可以合并
    boolean canMergeWith(Tile other){
        return !merged && other != null && !other.merged && value == other.getValue();
    }

    //合并两个方块，并返回合并后的值
    int mergeWith(Tile other){
        if(canMergeWith(other)){
            value*=2;
            merged=true;
            return value;
        }
        return -1;
    }
}

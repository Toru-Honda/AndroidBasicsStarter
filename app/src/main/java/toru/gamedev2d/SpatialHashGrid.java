package toru.gamedev2d;

import android.util.FloatMath;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Toru on 2015/05/04.
 * 2D Game用空間HashGridの実装
 */
public class SpatialHashGrid {
    static final int DEFAULT_NUM_OBJECTS_PER_CELL = 10; //Game Objectリストの初期サイズ。ガベージコレクタ対策として先に領域を確保しておく。

    List<GameObject>[] dynamicCells;
    List<GameObject>[] staticCells;
    int cellsPerRow;
    int cellsPerCol;
    float cellSize;
    int[] cellIds = new int[4]; //1つのGameObjectは最大4つのcellにしか属さないため。
    List<GameObject> foundObjects; //ガベージコレクタ対策としてメンバにする。

    /**
     * Constructor
     * @param worldWidth 2D game world width
     * @param worldHeight 2D game world height.
     * @param cellSize cell size(square).
     */
    @SuppressWarnings("unchecked")
    public SpatialHashGrid(float worldWidth, float worldHeight, float cellSize) {
        this.cellSize = cellSize;
        this.cellsPerRow = (int) FloatMath.ceil(worldWidth / cellSize);
        this.cellsPerCol = (int)FloatMath.ceil(worldHeight / cellSize);
        int numCells = cellsPerCol * cellsPerRow;
        dynamicCells = new List[numCells];
        staticCells = new List[numCells];
        for(int i = 0; i < numCells; i++) {
            dynamicCells[i] = new ArrayList<GameObject>(DEFAULT_NUM_OBJECTS_PER_CELL);
            staticCells[i] = new ArrayList<GameObject>(DEFAULT_NUM_OBJECTS_PER_CELL);
        }
        foundObjects = new ArrayList<GameObject>(DEFAULT_NUM_OBJECTS_PER_CELL);
    }

    /**
     * Insert GameObject to static cells.
     * @param obj
     */
    public void insertStaticObject(GameObject obj) {
        int[] cellIds = getCellIds(obj);
        int i = 0;
        int cellId = -1;
        while(i <= 3 && (cellId = cellIds[i++]) != -1) {
            staticCells[cellId].add(obj);
        }
    }

    /**
     * Insert GameObject to dynamic cells.
     * @param obj
     */
    public void insertDynamicObject(GameObject obj) {
        int[] cellIds = getCellIds(obj);
        int i = 0;
        int cellId = -1;
        while(i <= 3 && (cellId = cellIds[i++]) != -1) {
            dynamicCells[cellId].add(obj);
        }
    }

    /**
     * Remove GameObject from dynamic and static cells. "remove" is tried both array list.
     * @param obj
     */
    public void removeObject(GameObject obj) {
        int[] cellIds = getCellIds(obj);
        int i = 0; int cellId = -1;
        while(i <= 3 && (cellId = cellIds[i++]) != -1){
            dynamicCells[cellId].remove(obj);
            dynamicCells[cellId].remove(obj);
        }
    }

    /**
     * Clear lists in dynamicCells.
     */
    public void clearDynamicCells() {
        for(int i = 0; i < dynamicCells.length; i++){
            dynamicCells[i].clear();
        }
    }

    /**
     * Clear list in staticCells.
     */
    public void clearStaticCells() {
        for(int i = 0; i < staticCells.length; i++) {
            staticCells[i].clear();
        }
    }

    /**
     * Gets GameObject list contained same cell of obj.
     * @param obj
     * @return
     */
    public List<GameObject> getPotentialColliders(GameObject obj) {
        foundObjects.clear();
        int[] cellIds = getCellIds(obj);
        int i = 0; int cellId = -1;
        while(i <= 3 && (cellId = cellIds[i++]) != -1) {
            for(int j = 0; j < dynamicCells[cellId].size(); j++) {
                GameObject collider = dynamicCells[cellId].get(j);
                if(!foundObjects.contains(collider)) { foundObjects.add(collider); }
            }
            for(int j = 0; j < staticCells[cellId].size(); j++) {
                GameObject collider = staticCells[cellId].get(j);
                if(!foundObjects.contains(collider)) { foundObjects.add(collider); }
            }
        }
        return foundObjects;
    }

    public int[] getCellIds(GameObject obj) {
        int x1 = (int)FloatMath.floor(obj.bounds.lowerLeft.x / cellSize);
        int y1 = (int)FloatMath.floor(obj.bounds.lowerLeft.y / cellSize);
        int x2 = (int)FloatMath.floor((obj.bounds.lowerLeft.x + obj.bounds.width) / cellSize);
        int y2 = (int)FloatMath.floor((obj.bounds.lowerLeft.y + obj.bounds.height) / cellSize);

        if(x1 == x2 && y1 == y2) {
            //objが1つのcellにのみ存在する場合
            if(x1 >= 0 && x1 < cellsPerRow && y1 >= 0 && y1 < cellsPerCol) {
                cellIds[0] = x1 + y1 * cellsPerRow;
            } else {
                cellIds[0] = -1;
            }
            cellIds[1] = cellIds[2] = cellIds[3] = -1;
        } else if(x1 == x2) {
            //objが2つのcell(y方向)にまたがって存在する場合
            int i = 0;
            if(x1 >= 0 && x1 < cellsPerRow) {
                if(y1 >= 0 && y1 < cellsPerCol) {
                    cellIds[i++] = x1 + y1 * cellsPerRow;
                }
                if(y2 >= 0 && y2 < cellsPerCol) {
                    cellIds[i++] = x1 + y2 * cellsPerRow;
                }
            }
            while(i <= 3) { cellIds[i++] = -1; }
        } else if(y1 == y2) {
            //objが2つのcell(x方向)にまたがって存在する場合
            int i = 0;
            if(y1 >= 0 && y1 < cellsPerCol) {
                if(x1 >= 0 && x1 < cellsPerRow) {
                    cellIds[i++] = x1 + y1 * cellsPerRow;
                }
                if(x2 >= 0 && x2 < cellsPerRow) {
                    cellIds[i++] = x2 + y1 * cellsPerRow;
                }
            }
            while(i <= 3) { cellIds[i++] = -1; }
        } else {
            //objが4つのcellにまたがって存在する場合
            int i = 0;
            int y1CellsPerRow = y1 * cellsPerRow;
            int y2CellsPerRow = y2 * cellsPerRow;
            if(x1 >= 0 && x1 < cellsPerRow && y1 >= 0 && y1 < cellsPerCol) {
                cellIds[i++] = x1 + y1CellsPerRow;
            }
            if(x2 >= 0 && x2 < cellsPerRow && y1 >= 0 && y1 < cellsPerCol) {
                cellIds[i++] = x2 + y1CellsPerRow;
            }
            if(x1 >= 0 && x1 < cellsPerRow && y2 >= 0 && y2 < cellsPerCol) {
                cellIds[i++] = x1 + y2CellsPerRow;
            }
            if(x2 >= 0 && x2 < cellsPerRow && y2 >= 0 && y2 < cellsPerCol) {
                cellIds[i++] = x2 + y2CellsPerRow;
            }
            while(i <= 3) { cellIds[i++] = -1; }
        }
        return cellIds;
    }
}

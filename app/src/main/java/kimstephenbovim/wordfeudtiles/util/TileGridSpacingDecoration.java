package kimstephenbovim.wordfeudtiles.util;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

public class TileGridSpacingDecoration extends RecyclerView.ItemDecoration {

    private static final int VERTICAL = OrientationHelper.VERTICAL;

    Context context;
    int span;
    int pxSpacing;
    int pxExtraMargin;


    //TODO burde jeg bruke denne?
    public TileGridSpacingDecoration(Context context) {

        this.context = context;

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        //Brukes height som width når device er i landscape?
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        int dpWidth = (int) (displayMetrics.widthPixels / displayMetrics.density);

        int dpTileSize = 40;
        int dpMinMargin = 8;
        int dpMinSpacing = 3;

        int availableSpace = dpWidth - dpMinMargin - dpMinMargin + dpMinSpacing;

        span = availableSpace / (dpTileSize + dpMinSpacing);
        int dpSpacing = dpMinSpacing + (availableSpace % (dpTileSize + dpMinSpacing) / (span - 1));
        float dpExtraMargin = (availableSpace - ((dpTileSize + dpSpacing) * span)) / 2f;
        pxSpacing = Math.round(dpSpacing * displayMetrics.density);
        pxExtraMargin = Math.round(dpExtraMargin * displayMetrics.density);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        //int column = position % span; // item column

        //denne må settes
        int rackCount = 7;

        if (position == 0 || position == 1 || position == rackCount + 2) {
            System.out.println("RETURNERER PGA IKKE TILE");
            return;
        }

        outRect.top = pxSpacing;
        if ((rackCount > 0 && position == 2) || (position > rackCount + 2 && (position - rackCount) % span == 3)) {
            System.out.println("position: " + position + ", legger til extraMargin");
            outRect.left = pxExtraMargin;
        } else {
            outRect.left = pxSpacing;
        }

        /*if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = spacing;
            }
            outRect.bottom = spacing; // item bottom
        } else {*/
            /*outRect.left = column * pxSpacing / span; // column * ((1f / spanCount) * spacing)
            outRect.right = pxSpacing - (column + 1) * pxSpacing / span; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= span) {
                outRect.top = pxSpacing; // item top
            }*/
        //}
    }

    /*@Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        super.getItemOffsets(outRect, view, parent, state);

        int childCount = parent.getLayoutManager().getItemCount();
        int childIndex = parent.getChildAdapterPosition(view);

        int itemSpanSize = getItemSpanSize(parent, childIndex);
        int spanIndex = getItemSpanIndex(parent, childIndex);

        //setSpacings(outRect, parent, childCount, childIndex, itemSpanSize, spanIndex);
    }

    protected void setSpacings(Rect outRect, RecyclerView parent, int childCount, int childIndex, int itemSpanSize, int spanIndex) {

        outRect.top = pxSpacing;
        outRect.left = spanIndex == 0 ? 0 : pxSpacing;
    }*/

    /*@SuppressWarnings("all")
    protected int getTotalSpan(RecyclerView parent) {

        RecyclerView.LayoutManager mgr = parent.getLayoutManager();
        if (mgr instanceof GridLayoutManager) {
            return ((GridLayoutManager) mgr).getSpanCount();
        } else if (mgr instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) mgr).getSpanCount();
        } else if (mgr instanceof LinearLayoutManager) {
            return 1;
        }

        return -1;
    }*/

    /*@SuppressWarnings("all")
    protected int getItemSpanSize(RecyclerView parent, int childIndex) {

        RecyclerView.LayoutManager mgr = parent.getLayoutManager();
        if (mgr instanceof GridLayoutManager) {
            return ((GridLayoutManager) mgr).getSpanSizeLookup().getSpanSize(childIndex);
        } else if (mgr instanceof StaggeredGridLayoutManager) {
            return 1;
        } else if (mgr instanceof LinearLayoutManager) {
            return 1;
        }

        return -1;
    }

    @SuppressWarnings("all")
    protected int getItemSpanIndex(RecyclerView parent, int childIndex) {

        RecyclerView.LayoutManager mgr = parent.getLayoutManager();
        if (mgr instanceof GridLayoutManager) {
            return ((GridLayoutManager) mgr).getSpanSizeLookup().getSpanIndex(childIndex, spanCount);
        } else if (mgr instanceof StaggeredGridLayoutManager) {
            return childIndex % spanCount;
        } else if (mgr instanceof LinearLayoutManager) {
            return 0;
        }

        return -1;
    }

    @SuppressWarnings("all")
    protected int getOrientation(RecyclerView parent) {

        RecyclerView.LayoutManager mgr = parent.getLayoutManager();
        if (mgr instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) mgr).getOrientation();
        } else if (mgr instanceof GridLayoutManager) {
            return ((GridLayoutManager) mgr).getOrientation();
        } else if (mgr instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) mgr).getOrientation();
        }

        return VERTICAL;
    }

    protected boolean isLeftEdge(RecyclerView parent, int childCount, int childIndex, int itemSpanSize, int spanIndex) {

        if (orientation == VERTICAL) {

            return spanIndex == 0;

        } else {

            return (childIndex == 0) || isFirstItemEdgeValid((childIndex < spanCount), parent, childIndex);
        }
    }

    protected boolean isRightEdge(RecyclerView parent, int childCount, int childIndex, int itemSpanSize, int spanIndex) {

        if (orientation == VERTICAL) {

            return (spanIndex + itemSpanSize) == spanCount;

        } else {

            return isLastItemEdgeValid((childIndex >= childCount - spanCount), parent, childCount, childIndex, spanIndex);
        }
    }

    protected boolean isTopEdge(RecyclerView parent, int childCount, int childIndex, int itemSpanSize, int spanIndex) {

        if (orientation == VERTICAL) {

            return (childIndex == 0) || isFirstItemEdgeValid((childIndex < spanCount), parent, childIndex);

        } else {

            return spanIndex == 0;
        }
    }

    protected boolean isBottomEdge(RecyclerView parent, int childCount, int childIndex, int itemSpanSize, int spanIndex) {

        if (orientation == VERTICAL) {

            return isLastItemEdgeValid((childIndex >= childCount - spanCount), parent, childCount, childIndex, spanIndex);

        } else {

            return (spanIndex + itemSpanSize) == spanCount;
        }
    }

    protected boolean isFirstItemEdgeValid(boolean isOneOfFirstItems, RecyclerView parent, int childIndex) {

        int totalSpanArea = 0;
        if (isOneOfFirstItems) {
            for (int i = childIndex; i >= 0; i--) {
                totalSpanArea = totalSpanArea + getItemSpanSize(parent, i);
            }
        }

        return isOneOfFirstItems && totalSpanArea <= spanCount;
    }

    protected boolean isLastItemEdgeValid(boolean isOneOfLastItems, RecyclerView parent, int childCount, int childIndex, int spanIndex) {

        int totalSpanRemaining = 0;
        if (isOneOfLastItems) {
            for (int i = childIndex; i < childCount; i++) {
                totalSpanRemaining = totalSpanRemaining + getItemSpanSize(parent, i);
            }
        }

        return isOneOfLastItems && (totalSpanRemaining <= spanCount - spanIndex);
    }*/
}
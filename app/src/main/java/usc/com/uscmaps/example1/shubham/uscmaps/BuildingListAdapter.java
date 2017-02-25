package usc.com.uscmaps.example1.shubham.uscmaps;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import usc.com.uscmaps.example1.shubham.uscmaps.data.WaitListContract;

/**
 * Created by Shubham on 2/21/17.
 */

public class BuildingListAdapter extends RecyclerView.Adapter<BuildingListAdapter.BuildingViewHolder> {

    private Cursor mCursor;
    private Context mContext;
    private static ListItemClickListener mlistItemClickListener;
    private String[] mWeatherData;


    public BuildingListAdapter(Cursor mCursor, Context context, ListItemClickListener listener) {
        this.mCursor = mCursor;
        this.mContext = context;
        this.mlistItemClickListener = listener;

    }
//
//    public BuildingListAdapter(Context context, ListItemClickListener listener) {
//        this.mContext = context;
//        this.mlistItemClickListener = listener;
//
//    }

    @Override
    public BuildingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.building_list_item, parent, false);
        return new BuildingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BuildingViewHolder holder, int position) {
        if(!mCursor.moveToPosition(position)){
            return;
        }

        String name = mCursor.getString(mCursor.getColumnIndex(WaitListContract.WaitListEntry.COLUMN_BUILDING_NAME));
//        mWeatherData[0] = name;
        holder.bTextView.setText(name);

    }

    @Override
    public int getItemCount() {
//        Log.e("getItemCount", ""+mCursor.getCount());
        return mCursor.getCount();
    }


    class BuildingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView bTextView ;

        public BuildingViewHolder(View itemView){
            super(itemView);
            bTextView = (TextView) itemView.findViewById(R.id.buildingNameTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Log.e("Builddinglistadaprter", ""+adapterPosition);
            String weatherForDay = mWeatherData[adapterPosition];
            mlistItemClickListener.onClick(weatherForDay);

//            mlistItemClickListener.recyclerViewItemClicked(v, this.getLayoutPosition());
        }
    }

    /**
     * Creating a click listener for each item in Recycler View
     */
    public interface ListItemClickListener{
//        void recyclerViewItemClicked(View v, int clickedPosition);

        void onClick(String weatherForDay);
    }
}

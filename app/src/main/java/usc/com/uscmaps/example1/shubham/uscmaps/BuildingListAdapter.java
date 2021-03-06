package usc.com.uscmaps.example1.shubham.uscmaps;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import usc.com.uscmaps.example1.shubham.uscmaps.data.WaitListContract;



public class BuildingListAdapter extends RecyclerView.Adapter<BuildingListAdapter.BuildingViewHolder> {

    private Cursor mCursor;
    private Context mContext;
    private static ListItemClickListener mlistItemClickListener;
    public static final String TAG = BuildingListAdapter.class.getSimpleName();

    public BuildingListAdapter(Cursor mCursor, Context context, ListItemClickListener listener) {
        this.mCursor = mCursor;
        this.mContext = context;
        this.mlistItemClickListener = listener;
    }

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
        String address = mCursor.getString(mCursor.getColumnIndex(WaitListContract.WaitListEntry.COLUMN_ADDRESS));
        holder.bTextViewBuildingName.setText(name);
        holder.bTextViewAddress.setText(address);

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    class BuildingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView bTextViewBuildingName;
        TextView bTextViewAddress;

        public BuildingViewHolder(View itemView) {
            super(itemView);
            bTextViewBuildingName = (TextView) itemView.findViewById(R.id.buildingNameTextView);
            bTextViewAddress = (TextView) itemView.findViewById(R.id.buildingAddress);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            String selectedString = mCursor.getString(mCursor.getColumnIndex(WaitListContract.WaitListEntry.COLUMN_BUILDING_NAME));
            mlistItemClickListener.onClick(selectedString);


        }
    }

    /**
     * Creating a click listener for each item in Recycler View
     */
    public interface ListItemClickListener{
        void onClick(String weatherForDay);
    }
}

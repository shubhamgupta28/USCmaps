package usc.com.uscmaps.example1.shubham.uscmaps;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import usc.com.uscmaps.example1.shubham.uscmaps.data.WaitListContract;

/**
 * Created by Shubham on 2/28/17.
 */

public class ParkingListAdapter extends RecyclerView.Adapter<ParkingListAdapter.ParkingViewHolder>{
    private Cursor mCursor;
    private Context mContext;
    private static ListItemClickListenerParking mlistItemClickListener;
    public static final String TAG = ParkingListAdapter.class.getSimpleName();

    public ParkingListAdapter(Cursor mCursor, Context context, ListItemClickListenerParking listener) {
        this.mCursor = mCursor;
        this.mContext = context;
        this.mlistItemClickListener = listener;
    }

    @Override
    public ParkingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.parking_list_item, parent, false);
        return new ParkingListAdapter.ParkingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ParkingViewHolder holder, int position) {
        if(!mCursor.moveToPosition(position)){
            return;
        }
//        Log.e(TAG, ""+mCursor.getCount());
        String name = mCursor.getString(mCursor.getColumnIndex(WaitListContract.WaitListEntry.COLUMN_NAME_PARKING));
        holder.bTextView.setText(name);

    }

    @Override
    public int getItemCount() {
//        Log.e(TAG, ""+mCursor.getCount());
        return mCursor.getCount();
    }


    class ParkingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView bTextView ;

        public ParkingViewHolder(View itemView) {
            super(itemView);
            bTextView = (TextView) itemView.findViewById(R.id.parkingNameTextView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
//            Log.e(TAG, ""+adapterPosition);
            mCursor.moveToPosition(adapterPosition);
            String selectedString = mCursor.getString(mCursor.getColumnIndex(WaitListContract.WaitListEntry.COLUMN_NAME_PARKING));
            mlistItemClickListener.onClick(selectedString);


        }
    }

    /**
     * Creating a click listener for each item in Recycler View
     */
    public interface ListItemClickListenerParking{
        void onClick(String weatherForDay);
    }
}

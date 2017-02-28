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
 * Created by Shubham on 2/21/17.
 */

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
        holder.bTextView.setText(name);

//        holder.itemView.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                //Here goes your desired onClick behaviour. Like:
//                Toast.makeText(view.getContext(), "You have clicked " + view.getId(), Toast.LENGTH_SHORT).show(); //you can add data to the tag of your cardview in onBind... and retrieve it here with with.getTag().toString()..
//                //You can change the fragment, something like this, not tested, please correct for your desired output:
//                AppCompatActivity activity = (AppCompatActivity) view.getContext();
//                Tab2Map myFragment = new Tab2Map();
//                //Create a bundle to pass data, add data, set the bundle to your fragment and:
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
//            }
//        });

//        AppCompatActivity activity = (AppCompatActivity) getContext();
//        Tab2Map myFragment = new Tab2Map();
//        //Create a bundle to pass data, add data, set the bundle to your fragment and:
//        activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    class BuildingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView bTextView ;

        public BuildingViewHolder(View itemView) {
            super(itemView);
            bTextView = (TextView) itemView.findViewById(R.id.buildingNameTextView);
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
//            Log.e(TAG, ""+adapterPosition);
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

package tagbin.in.myapplication.UpcomingRides;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

import tagbin.in.myapplication.R;

/**
 * Created by admin pc on 16-12-2015.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyviewHolder> {

    private ArrayList<DataItems> infoList = new ArrayList<>();
    private LayoutInflater mInflater;

    public MyAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void setMovies(ArrayList<DataItems> list,Boolean b) {
        this.infoList = list;
        //update the adapter to reflect the new set of movies
        notifyDataSetChanged();
    }


    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.myrow, parent, false);
        MyviewHolder viewHolder = new MyviewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyviewHolder holder, int position) {
        DataItems currentItem = infoList.get(position);
        holder.cab_no.setText(currentItem.getCab_no());
        holder.time.setText(currentItem.getTime());
        holder.to_loc.setText(currentItem.getTo_loc());
        holder.pick.setText(currentItem.getPick());

    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    static class MyviewHolder extends RecyclerView.ViewHolder {
        TextView cab_no;
        TextView time;
        TextView to_loc;
        TextView pick;


        public MyviewHolder(View itemView) {
            super(itemView);
            cab_no = (TextView) itemView.findViewById(R.id.cab_no);
            time = (TextView) itemView.findViewById(R.id.time);
            to_loc = (TextView) itemView.findViewById(R.id.to_location);
            pick = (TextView) itemView.findViewById(R.id.pick);


        }
    }
}

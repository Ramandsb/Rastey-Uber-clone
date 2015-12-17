package tagbin.in.myapplication.UpcomingRides;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import tagbin.in.myapplication.R;

/**
 * Created by admin pc on 16-12-2015.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyviewHolder> {

    private ArrayList<DataItems> infoList = new ArrayList<>();
    private LayoutInflater mInflater;
    Context context;
    DataItems currentItem;
    int pos=0;

    public MyAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context=context;
    }

    public void setData(ArrayList<DataItems> list,Boolean b) {
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
    public void onBindViewHolder(MyviewHolder holder, final int position) {
         currentItem = infoList.get(position);
//        holder.click.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Toast.makeText(context, "Clicked: " + position+"///"+currentItem.getCab_no(), Toast.LENGTH_SHORT).show();
//
//            }
//        });
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
        Button click;


        public MyviewHolder(View itemView) {
            super(itemView);
            cab_no = (TextView) itemView.findViewById(R.id.mcab_no);
            time = (TextView) itemView.findViewById(R.id.mtime);
            to_loc = (TextView) itemView.findViewById(R.id.mto_location);
            pick = (TextView) itemView.findViewById(R.id.pick);
            click= (Button) itemView.findViewById(R.id.click);


        }
    }
}

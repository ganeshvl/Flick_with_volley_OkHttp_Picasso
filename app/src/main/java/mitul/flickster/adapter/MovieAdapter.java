package mitul.flickster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.Iterator;

import mitul.flickster.R;
import mitul.flickster.model.Flick;
import mitul.flickster.services.MySingleton;

public class MovieAdapter extends BaseAdapter {
    private Context mContext;
    private Flick[] movie;
    private ArrayList<Flick> movie1 = new ArrayList<Flick>();


    public MovieAdapter(Context context,Flick[] flick){
        mContext = context;
        movie = flick;
    }
    public MovieAdapter(Context context,ArrayList<Flick> flick){
        mContext = context;
        Iterator<Flick> itr = flick.iterator();
        while(itr.hasNext()){
            movie1.add(itr.next());
        }

    }

    @Override
    public int getCount() {
        //return movie.length;
        return movie1.size();
    }

    @Override
    public Object getItem(int position) {
        //return movie[position];
        return movie1.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.movie_list_item, null);
            holder = new ViewHolder();
            holder.movie_image = (NetworkImageView) convertView.findViewById(R.id.movie_image);
            holder.movie_title_label = (TextView) convertView.findViewById(R.id.movie_title);
            holder.content_type = (TextView) convertView.findViewById(R.id.content_type);
            holder.movie_short_plot_label = (TextView) convertView.findViewById(R.id.movie_short_plot);
            holder.genre = (TextView) convertView.findViewById(R.id.genre);
            holder.release_date = (TextView) convertView.findViewById(R.id.release_date);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }

        //Flick flick = movie[position];
        Flick flick = movie1.get(position);
        //Picasso.with(mContext).load("http://images.nationalgeographic.com/wpf/media-live/photos/000/005/cache/gray-kangaroo_554_600x450.jpg").into(holder.movie_image);
        //holder.movie_image.setImageResource(flick.getImageId());
        ImageLoader imL = MySingleton.getInstance(mContext).getImageLoader();
        holder.movie_image.setImageUrl(flick.getPoster(), imL);
        //Picasso.with(mContext).load(flick.getPoster()).into(holder.movie_image);
        holder.movie_title_label.setText(flick.getTitle());
        holder.content_type.setText(flick.getRated());
        holder.movie_short_plot_label.setText(flick.getShort_plot());
        holder.genre.setText(flick.getGenre());
        holder.release_date.setText(flick.getReleased());

        return convertView;
    }

    private static class ViewHolder{
        NetworkImageView movie_image;
        TextView movie_title_label;
        TextView content_type;
        TextView movie_short_plot_label;
        TextView genre;
        TextView release_date;
    }
}

package com.example.prudentialfinance.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager.widget.PagerAdapter;
import com.example.prudentialfinance.R;

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){
        this.context = context;
    }

    public int[] slide_images = {
            R.drawable.device,
            R.drawable.device2
    };

    public String[] slide_headings = {
            "Hỗ trợ quản lý chi tiêu",
            "Hỗ trợ các thông tin về tài khoản cá nhân",
    };

    public  String[] slide_desc = {
            "Quản lý các tài khoản cá nhân, thêm các danh mục chi tiêu, các mục tiêu tài chính bản thân muốn thiết lập.",
            "Thống kê các chi tiêu của bản thân, các mục tiêu cá nhân đạt được cũng như chưa đạt được.",
    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view  = layoutInflater.inflate(R.layout.slide_layout, container, false);


        // Set control
        ImageView imageView =  (ImageView) view.findViewById(R.id.image_slide);
        TextView titleView =  (TextView) view.findViewById(R.id.title_slide);
        TextView descView =  (TextView) view.findViewById(R.id.description_slide);


        // Set event
        imageView.setImageResource(slide_images[position]);
        titleView.setText(slide_headings[position]);
        descView.setText(slide_desc[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}

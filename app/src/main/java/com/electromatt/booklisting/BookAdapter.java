package com.electromatt.booklisting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list_item, parent, false);
        }

        Book currentBook = getItem(position);

        String bookTitle = currentBook.getTitle();
        TextView titleView = (TextView) listItemView.findViewById(R.id.book_title);
        titleView.setText(bookTitle);

        String bookAuthors = currentBook.getAuthors();
        TextView authorView = (TextView) listItemView.findViewById(R.id.book_author);
        authorView.setText(bookAuthors);

        return listItemView;
    }
}

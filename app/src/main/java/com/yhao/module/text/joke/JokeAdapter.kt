package com.yhao.module.pic

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.yhao.model.bean.Joke
import com.yhao.model.bean.Rhesis
import com.yhao.module.R
import org.jetbrains.anko.find

/**
 * Created by yhao on 17-9-6.
 *
 */
class JokeAdapter(var items: List<Joke>?) : RecyclerView.Adapter<JokeAdapter.MyViewHolder>() {


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = items?.get(position)?.title
        holder.textView.text = items?.get(position)?.text
    }


    override fun getItemCount(): Int = items?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder? {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_joke, parent, false))
    }

    class MyViewHolder(val item: View) : RecyclerView.ViewHolder(item) {
        val textView: TextView = item.find(R.id.text)
        val title: TextView = item.find(R.id.title)
    }


}
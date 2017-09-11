package com.yhao.module.pic

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.yhao.model.bean.Joke
import com.yhao.model.bean.Rhesis
import com.yhao.module.R
import org.jetbrains.anko.find
import java.util.Collections.replaceAll
import java.util.regex.Pattern
import java.util.regex.Pattern.compile


/**
 * Created by yhao on 17-9-6.
 *
 */
class JokeAdapter(var items: List<Joke>?) : RecyclerView.Adapter<JokeAdapter.MyViewHolder>() {

    @SuppressLint("WrongConstant")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = items?.get(position)?.title
        //原数据有html标签，过滤掉
        val p_html = compile("<[^>]+>", Pattern.CASE_INSENSITIVE)
        val m_html = p_html.matcher(items?.get(position)?.text)
        holder.textView.text = m_html.replaceAll("")

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
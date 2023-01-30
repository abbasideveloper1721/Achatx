package com.example.achatx.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.achatx.Models.MessagesModel;
import com.example.achatx.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter{

    ArrayList<MessagesModel> messagesModels;
    Context context;

    int SENDER_VIEW_TYPE = 1;
    int RECIVER_VIEW_TYPE = 2;

    public ChatAdapter(ArrayList<MessagesModel> messagesModels, Context context) {

        this.messagesModels = messagesModels;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SENDER_VIEW_TYPE)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
            return new SenderViewHolder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.sample_reciver,parent,false);
            return new ReciverViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(messagesModels.get(position).getuID().equals(FirebaseAuth.getInstance().getUid()))
        {
            return SENDER_VIEW_TYPE;
        }
        else {
            return RECIVER_VIEW_TYPE;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessagesModel messagesModel = messagesModels.get(position);
        if(holder.getClass()==SenderViewHolder.class){
            ((SenderViewHolder)holder).senderMsg.setText(messagesModel.getMessage());

        }else
            ((ReciverViewHolder)holder).reciverMsg.setText(messagesModel.getMessage());

    }

    @Override
    public int getItemCount() {
        return messagesModels.size();
    }

    public class ReciverViewHolder extends RecyclerView.ViewHolder {
        TextView reciverMsg,reciverTime;
        public ReciverViewHolder(@NonNull View itemView) {


            super(itemView);
            reciverMsg = itemView.findViewById(R.id.reciverMsgtext);
            reciverTime = itemView.findViewById(R.id.recivertexttime);
        }
    }
    public class SenderViewHolder extends RecyclerView.ViewHolder{
        TextView senderMsg,senderTime;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg = itemView.findViewById(R.id.senderText);
            senderTime = itemView.findViewById(R.id.senderTime);
        }
    }


}

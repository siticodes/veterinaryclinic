package com.example.vet;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

public class PetAdapter extends FirebaseRecyclerAdapter<PetModel,PetAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public PetAdapter(@NonNull FirebaseRecyclerOptions options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull PetModel model) {
        holder.petName.setText(model.getPetName());
        holder.petType.setText(model.getPetType());
        holder.petSex.setText(model.getPetSex());
        holder.petDOB.setText(model.getPetDOB());
        holder.petSurl.setText(model.getPetSurl());
        holder.petOwnerIC.setText(model.getPetOwnerIC());

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.petName.getContext())
                        .setContentHolder(new ViewHolder(R.layout.updatepet_popup))
                        .setExpanded(true, 1200)
                        .create();

                View view = dialogPlus.getHolderView();

                EditText petName = view.findViewById(R.id.petName);
                EditText petType = view.findViewById(R.id.petType);
                EditText petSex = view.findViewById(R.id.petSex);
                EditText petBreed = view.findViewById(R.id.petBreed);
                EditText petDOB = view.findViewById(R.id.petDOB);
                EditText petSurl = view.findViewById(R.id.petSurl);
                EditText petOwnerIC = view.findViewById(R.id.petOwnerIC);

                Button btnUpdate = view.findViewById(R.id.btnUpdate);

                petName.setText(model.getPetName());
                petType.setText(model.getPetType());
                petSex.setText(model.getPetSex());
                petBreed.setText(model.getPetBreed());
                petDOB.setText(model.getPetDOB());
                petSurl.setText(model.getPetSurl());
                petOwnerIC.setText(model.getPetOwnerIC());

                dialogPlus.show();

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("petName", petName.getText().toString());
                        map.put("petType", petType.getText().toString());
                        map.put("petSex", petSex.getText().toString());
                        map.put("petBreed", petBreed.getText().toString());
                        map.put("petDOB", petDOB.getText().toString());
                        map.put("petSurl", petSurl.getText().toString());
                        map.put("petOwnerIC", petOwnerIC.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("pet")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.petName.getContext(), "Data Updated Successfully.", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(holder.petName.getContext(), "Error While Updating.", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.petName.getContext());
                builder.setTitle("Are you Sure?");
                builder.setMessage("Deleted data can't be undo.");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("pet")
                                .child(getRef(position).getKey()).removeValue();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(holder.petName.getContext(), "Cancelled.", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }


        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adminpet_item,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView petName, petType, petSex, petBreed, petDOB, petSurl, petOwnerIC;

        Button btnEdit, btnDelete;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            petName = (TextView) itemView.findViewById(R.id.petName);
            petType = (TextView) itemView.findViewById(R.id.petType);
            petSex = (TextView) itemView.findViewById(R.id.petSex);
            petBreed = (TextView) itemView.findViewById(R.id.petBreed);
            petDOB = (TextView) itemView.findViewById(R.id.petDOB);
            petSurl = (TextView) itemView.findViewById(R.id.petSurl);
            petOwnerIC = (TextView) itemView.findViewById(R.id.petOwnerIC);

            btnEdit = (Button) itemView.findViewById(R.id.btnEdit);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);

        }
    }
}
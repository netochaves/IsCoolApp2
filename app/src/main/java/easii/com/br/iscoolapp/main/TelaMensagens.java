package easii.com.br.iscoolapp.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import easii.com.br.iscoolapp.R;
import easii.com.br.iscoolapp.adapters.MessagesListAdapter;
import easii.com.br.iscoolapp.constantes.URL;
import easii.com.br.iscoolapp.objetos.Aluno2;
import easii.com.br.iscoolapp.objetos.DatabaseHelper;
import easii.com.br.iscoolapp.objetos.Message;
import easii.com.br.iscoolapp.objetos.Professor2;
import easii.com.br.iscoolapp.util.DividerItemDecoration;

public class TelaMensagens extends AppCompatActivity implements View.OnClickListener{

    private Long idDisciplina = null;

    private EditText etMessage;
    private ImageButton btSendMessage;

    private List<Message> listademensagens = new ArrayList<Message>();
    private Toolbar toolbar;

    private EventBus bus;
    private Aluno2 aluno2;
    private Professor2 professor2;
    private String idUser = "";
    private String nomeUser = "";
    private String nomeDisciplina;

    private DatabaseHelper helper;
    FirebaseFirestore db;
    FirestoreRecyclerAdapter FirestoreAdapter;
    RecyclerView recyclerViewMsg;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_mensagens);
        FirebaseFirestore.setLoggingEnabled(true);
        acessaSharedPreferences();
        helper = new DatabaseHelper(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        idDisciplina = bundle.getLong("EXTRA_ID_DISCIPLINA");
        nomeDisciplina= bundle.getString("EXTRA_NOME_DISCIPLINA");

        recyclerViewMsg = (RecyclerView) findViewById(R.id.recycleView_messages);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMsg);
        toolbar.setTitle(nomeDisciplina);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(nomeDisciplina);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
        linearLayoutManager.setStackFromEnd(true);
        recyclerViewMsg.setLayoutManager(linearLayoutManager);


        db = FirebaseFirestore.getInstance();
        etMessage = (EditText) findViewById(R.id.et_message);
        btSendMessage = (ImageButton) findViewById(R.id.bt_send_message);

        btSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarMsg();
                etMessage.setText("");
            }
        });
        getMsg();

    }
    public void enviarMsg(){
        Date data = new Date();
        Map<String, Object> msg = new HashMap<>();
        msg.put("id", idUser);
        msg.put("msg", etMessage.getText().toString());
        msg.put("user", nomeUser);
        msg.put("hora",new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime()));
        msg.put("data", data);

        db.collection(nomeDisciplina).document(data.toString()).set(msg)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("LOG", "Escrita realizada com sucesso");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("LOG", "Falha na escrita");
                    }
                });
    }
    public void getMsg(){

        Query query = db.collection(nomeDisciplina);
        final FirestoreRecyclerOptions<Message> response = new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .build();
        FirestoreAdapter = new FirestoreRecyclerAdapter<Message, MessageHolder>(response) {

            @Override
            protected void onBindViewHolder(MessageHolder holder, int position, final Message model) {
                if(holder.getItemViewType() == 0) {
                    holder.msgText.setText(model.getMsg());
                    holder.msgUser.setText(model.getUser());
                    holder.msgTime.setText(model.getHora());
                }else{
                    holder.sendMsgText.setText(model.getMsg());
                    holder.sendMsgTime.setText(model.getHora());
                }
            }

            @Override
            public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                if(viewType == 0){
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.message_layout,parent,false);
                    return new MessageHolder(view);
                }else if(viewType == 1){
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.message_sent,parent,false);
                    return new MessageHolder(view);
                }
                return null;
            }

            @Override
            public int getItemViewType(int position) {
               Message message =  response.getSnapshots().get(position);
               if(message.getId().equals(idUser))
                   return 1;
               else
                   return 0;
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };
        FirestoreAdapter.notifyDataSetChanged();
        recyclerViewMsg.setAdapter(FirestoreAdapter);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tela_mensagens, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private String horaMsg() {
        Log.i("LOG", "hora" + System.currentTimeMillis());
        return "" + System.currentTimeMillis();
    }
    private void acessaSharedPreferences() {

        SharedPreferences sharedPref = TelaMensagens.this.getSharedPreferences("CONSTANTES", Context.MODE_PRIVATE);
        String tipo = sharedPref.getString("TIPO", "NULL");

        if (tipo.equals("ALUNO")) {

            String alunoJson = sharedPref.getString("ALUNO", "NULL");
            Gson gson = new Gson();
            aluno2 = gson.fromJson(alunoJson, Aluno2.class);
            idUser = "" + aluno2.getId();
            nomeUser = aluno2.getNome();
        } else {
            String profJson = sharedPref.getString("PROFESSOR", "NULL");
            Gson gson = new Gson();
            professor2 = gson.fromJson(profJson, Professor2.class);
            idUser = "" + professor2.getId();
            nomeUser = professor2.getNome();
        }
    }

    @Override
    public void onClick(View v) {
        enviarMsg();
    }

    public class MessageHolder extends RecyclerView.ViewHolder{

        TextView msgUser = (TextView) itemView.findViewById(R.id.msgUser);
        TextView msgText = (TextView) itemView.findViewById(R.id.msgText);
        TextView msgTime = (TextView) itemView.findViewById(R.id.msgTime);
        TextView sendMsgText = (TextView) itemView.findViewById(R.id.sendMsgText);
        TextView sendMsgTime = (TextView) itemView.findViewById(R.id.sendMsgTime);

        public MessageHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirestoreAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirestoreAdapter.stopListening();
    }
}


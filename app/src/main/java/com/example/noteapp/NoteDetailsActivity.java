package com.example.noteapp;



import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.Context;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;

import java.sql.Time;
import java.util.Calendar;


import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetailsActivity extends AppCompatActivity {

    EditText titleEditText, contentEditText;
    ImageButton saveNoteBtn;
    TextView pageTitleTextView, tvDate, tvTime;
    String title, content, docId;
    boolean isEditMode = false;
    TextView deleteNoteTextViewBtn;
    Calendar calendar;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        calendar = Calendar.getInstance();

        titleEditText = findViewById(R.id.notes_title_text);
        contentEditText = findViewById(R.id.notes_content_text);
        saveNoteBtn = findViewById(R.id.save_note_btn);
        pageTitleTextView = findViewById(R.id.page_title);
        deleteNoteTextViewBtn = findViewById(R.id.delete_note_text_view_btn);

        // receive data
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        if (docId != null && !docId.isEmpty()) {
            isEditMode = true;
        }

        titleEditText.setText(title);
        contentEditText.setText(content);
        if (isEditMode) {
            pageTitleTextView.setText("Edit your note");
            deleteNoteTextViewBtn.setVisibility(View.VISIBLE);
        }

        saveNoteBtn.setOnClickListener(v -> saveNote());
        deleteNoteTextViewBtn.setOnClickListener(v -> deleteNoteFromFirebase());

        tvDate.setOnClickListener(v -> tvDate());
        tvTime.setOnClickListener(v -> tvTime());
    }

    @SuppressLint("SetTextI18n")
    private void updateDateTimeViews() {
        String date = calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                (calendar.get(Calendar.MONTH) + 1) + "/" +
                calendar.get(Calendar.YEAR);

        @SuppressLint("DefaultLocale") String time = calendar.get(Calendar.HOUR_OF_DAY) + ":" +
                String.format("%02d", calendar.get(Calendar.MINUTE));

        tvDate.setText("Ngày: " + date);
        tvTime.setText("Giờ: " + time);
    }

    public void tvDate() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year1);
            calendar.set(Calendar.MONTH, month1);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateTimeViews();
        }, year, month, day);

        datePickerDialog.show();
    }

    public void tvTime() {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute1);
            updateDateTimeViews();
        }, hour, minute, true);

        timePickerDialog.show();
    }

    void saveNote() {
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();
        String noteDate = tvDate.getText().toString();
        String noteTime = tvTime.getText().toString();

        if (noteTitle == null || noteTitle.isEmpty()) {
            titleEditText.setError("Title is required");
            return;
        }
        if (noteContent == null || noteContent.isEmpty()) {
            contentEditText.setError("Content is required");
            return;
        }

        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());
        note.setTVdate(noteDate);
        note.setTVtime(noteTime);

        saveNoteToFirebase(note);
    }

    @SuppressLint("ScheduleExactAlarm")
    private void setReminder() {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, ReminderBroadcastReceiver.class);
        intent.setAction("MyNote");
//        intent.putExtra("content",content.toString());
        intent.putExtra("time", tvTime.getText().toString());

        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            // Set the alarm to the specified time
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    void saveNoteToFirebase(Note note) {
        DocumentReference documentReference;
        if (isEditMode) {
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        } else {
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }

        documentReference.set(note).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Set reminder after saving the note
                setReminder();
                Utility.showToast(NoteDetailsActivity.this, "Note added successfully");
                finish();
            } else {
                Utility.showToast(NoteDetailsActivity.this, "Failed while adding note");
            }
        });
    }

    void deleteNoteFromFirebase() {
        DocumentReference documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        documentReference.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Utility.showToast(NoteDetailsActivity.this, "Note deleted successfully");
                finish();
            } else {
                Utility.showToast(NoteDetailsActivity.this, "Failed while deleting note");
            }
        });
    }
}






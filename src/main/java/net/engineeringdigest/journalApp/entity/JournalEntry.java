package net.engineeringdigest.journalApp.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "journal_entries")
@Data // Using @Data will not add @NoArgsConstructor, so we have to add @NoArgsConstructor explicitly
@NoArgsConstructor // We're using this annotation because this is used to deserialize the JSON data into Java object, and it requires a no-argument constructor to create an instance of the class before setting the fields.
public class JournalEntry {
    @Id
    private ObjectId id;

    @NonNull
    private String title;
    private String content;

    private LocalDateTime Date;
}
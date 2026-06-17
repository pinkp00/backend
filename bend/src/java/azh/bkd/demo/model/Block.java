package azh.bkd.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "blocks")
@Data
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String blockName; // e.g., "Block B (Computer Science)", "Block M"
}
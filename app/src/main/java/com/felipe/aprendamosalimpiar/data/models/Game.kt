import androidx.room.Entity
import androidx.room.PrimaryKey
import com.felipe.aprendamosalimpiar.data.models.Dificultad
import com.felipe.aprendamosalimpiar.data.models.NivelJuego

@Entity(tableName = "games")
data class Game(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val patientId: Long,  // Relación con el paciente
    val date: String,     // Formato simple: "dd/MM/yyyy HH:mm"
    val difficulty: Dificultad,  // Usa tu enum existente
    val lastLevel : NivelJuego
)
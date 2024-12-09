import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.petlife.databinding.TilePetBinding
import com.example.petlife.model.pet.Pet

class PetAdapter(
    private val petList: List<Pet>,
    private val onContextMenuRequested: (Int) -> Unit,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<PetAdapter.PetViewHolder>() {

    inner class PetViewHolder(val binding: TilePetBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnCreateContextMenuListener {

        init {
            binding.root.setOnClickListener {
                onItemClick(adapterPosition)
            }

            binding.root.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
            onContextMenuRequested(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val binding = TilePetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        val pet = petList[position]
        holder.binding.nameItemTv.text = pet.name
        holder.binding.typeItemTv.text = pet.type.name
    }

    override fun getItemCount(): Int = petList.size
}

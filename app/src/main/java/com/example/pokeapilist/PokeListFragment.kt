package com.example.pokeapilist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokeapilist.databinding.FragmentPokeListBinding
import kotlinx.coroutines.launch
import retrofit2.Retrofit

class PokeListFragment: Fragment() {
    private var _binding: FragmentPokeListBinding? = null
    private val binding get() = _binding!! //Returns the outermost View in the associated layout file.

    private lateinit var adapter: PokemonListAdapter
    private val pokeList = mutableListOf<PokemonResponse>()

    //[Return] the View for the fragment's UI, or null.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPokeListBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.PokeListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = PokemonListAdapter(pokeList)
        binding.PokeListRecyclerView.adapter = adapter

        lifecycleScope.launch {
            for (id in 1..10) {
                try {
                    val pokemon = RetrofitInstance.api.getPokemon(id)
                    pokeList.add(pokemon)
                    adapter.notifyItemInserted(pokeList.size - 1)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
package com.example.pokeapilist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.pokeapilist.databinding.PokeRowBinding
import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

//RecyclerView のアイテムは LayoutManager クラスによって並べ替えられます
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window,true)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PokeListFragment())
                .commit()
        }
    }
}

class PokemonListAdapter(private val pokemonList: MutableList<PokemonResponse>): RecyclerView.Adapter<PokemonListAdapter.ViewHolder>(){

    //RecyclerViewはView自体に当たる
    //各リストの項目はViewHolderによって定義される。ViewHolderの引数はバインディングされたViewの各コンポーネント。
    class ViewHolder(val binding: PokeRowBinding): RecyclerView.ViewHolder(binding.root)

    //RecyclerViewがアイテムを表現するために、与えられた型の新しいViewHolderを必要とするときに呼び出されます。
    //XMLレイアウトファイルから作成することができます。
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("MyLog","onCreateViewHolderが呼ばれました")
        val binding = PokeRowBinding.inflate(
            LayoutInflater.from(parent.context),parent, false
        )
        return ViewHolder(binding)
    }

    //ViewHolderに渡されてきたデータのカウント。
    override fun getItemCount(): Int = pokemonList.size - 1

    // ViewHolderの表示内容を、 API通信して取得した情報を元に更新する。
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("MyLog","onBindViewHolderが呼ばれました")
        val pokemon = pokemonList[position]
        holder.binding.textView.text = pokemon.name
        Glide.with(holder.binding.imageView)
            .load(pokemon.sprites.frontDefault)
            .into(holder.binding.imageView)
    }
}

data class PokemonResponse(
    val name: String,
    val sprites: Sprites
)

data class Sprites(
    @SerializedName("front_default")
    val frontDefault: String?
)

interface PokeAPIService{
    @GET("pokemon/{id}")
    suspend fun getPokemon(@Path("id") id: Int): PokemonResponse
}


object RetrofitInstance {
    val api: PokeAPIService by lazy {
        Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokeAPIService::class.java)
    }
}
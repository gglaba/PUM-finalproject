package com.example.mymusiclibrary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymusiclibrary.databinding.FragmentListBinding

class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val exercises: MutableList<Album> = getAlbumsList(requireContext()).toMutableList()
        binding.albumRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = AlbumAdapter(exercises)
        }

        binding.addButton.setOnClickListener {
            if (binding.editTitleText.text?.isNotEmpty() == true) {
                exercises.add(
                    Album(
                        binding.editTitleText.text.toString(),
                        "Artist\n\nabout",
                        ""
                    )
                )
                saveAlbumsList(requireContext(), exercises)

                binding.albumRecyclerView.adapter?.notifyDataSetChanged()
                binding.editTitleText.text?.clear()
            }
        }
    }

}
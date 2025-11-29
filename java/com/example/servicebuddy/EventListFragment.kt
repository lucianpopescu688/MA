package com.example.servicebuddy

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class EventListFragment : Fragment() {

    private val viewModel: EventViewModel by activityViewModels()
    private lateinit var eventAdapter: EventAdapter
    private lateinit var allEvents: List<MaintenanceEvent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_event_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMenu()

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        eventAdapter = EventAdapter { event ->
            val action = EventListFragmentDirections.actionToEditEventFragment(event.id)
            findNavController().navigate(action)
        }
        recyclerView.adapter = eventAdapter

        viewModel.events.observe(viewLifecycleOwner) { events ->
            allEvents = events
            eventAdapter.submitList(events)
        }

        val searchEditText = view.findViewById<EditText>(R.id.etSearch)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterEvents(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.event_list_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_add -> {
                        findNavController().navigate(R.id.action_to_addEventFragment)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun filterEvents(query: String) {
        val filteredList = if (query.isEmpty()) {
            allEvents
        } else {
            allEvents.filter {
                it.title.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault())) ||
                it.vehicleIdentifier.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault()))
            }
        }
        eventAdapter.submitList(filteredList)
    }
}
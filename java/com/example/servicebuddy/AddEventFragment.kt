package com.example.servicebuddy

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

class AddEventFragment : Fragment() {

    private val viewModel: EventViewModel by activityViewModels()
    private val calendar: Calendar = Calendar.getInstance()

    private lateinit var layoutId: TextInputLayout
    private lateinit var etId: TextInputEditText
    private lateinit var layoutTitle: TextInputLayout
    private lateinit var etTitle: TextInputEditText
    private lateinit var layoutVehicle: TextInputLayout
    private lateinit var etVehicle: TextInputEditText
    private lateinit var etDescription: TextInputEditText
    private lateinit var layoutPrice: TextInputLayout
    private lateinit var etPrice: TextInputEditText
    private lateinit var etDueDate: TextInputEditText
    private lateinit var toggleCategory: MaterialButtonToggleGroup
    private lateinit var actvStatus: AutoCompleteTextView
    private lateinit var btnSave: Button
    private lateinit var ivClose: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findViews(view)

        val statuses = resources.getStringArray(R.array.status_options)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, statuses)
        actvStatus.setAdapter(adapter)
        actvStatus.setText(statuses[2], false) // Set "UPCOMING" as default

        etId.setText(UUID.randomUUID().toString())
        updateDateInView()

        toggleCategory.check(R.id.btn_service)

        ivClose.setOnClickListener {
            findNavController().popBackStack()
        }

        etDueDate.setOnClickListener {
            showDatePickerDialog()
        }

        btnSave.setOnClickListener {
            if (validateInput()) {
                saveEvent()
            }
        }
    }

    private fun findViews(view: View) {
        layoutId = view.findViewById(R.id.layout_id)
        etId = view.findViewById(R.id.et_id)
        layoutTitle = view.findViewById(R.id.layout_title)
        etTitle = view.findViewById(R.id.et_title)
        layoutVehicle = view.findViewById(R.id.layout_vehicle)
        etVehicle = view.findViewById(R.id.et_vehicle)
        etDescription = view.findViewById(R.id.et_description)
        layoutPrice = view.findViewById(R.id.layout_price)
        etPrice = view.findViewById(R.id.et_price)
        etDueDate = view.findViewById(R.id.et_due_date)
        toggleCategory = view.findViewById(R.id.toggle_category)
        actvStatus = view.findViewById(R.id.actv_status)
        btnSave = view.findViewById(R.id.btn_save)
        ivClose = view.findViewById(R.id.ivClose)
    }

    private fun showDatePickerDialog() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }

        DatePickerDialog(
            requireContext(),
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateDateInView() {
        val myFormat = "MMMM dd, yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        etDueDate.setText(sdf.format(calendar.time))
    }

    private fun validateInput(): Boolean {
        var isValid = true
        val id = etId.text.toString()

        if (id.isNullOrEmpty()) {
            layoutId.error = "ID is required"
            isValid = false
        } else {
            try {
                UUID.fromString(id)
                if (viewModel.getEventById(id) != null) {
                    layoutId.error = "ID already exists"
                    isValid = false
                } else {
                    layoutId.error = null
                }
            } catch (e: IllegalArgumentException) {
                layoutId.error = "Invalid UUID format"
                isValid = false
            }
        }

        if (etTitle.text.isNullOrEmpty()) {
            layoutTitle.error = "Title is required"
            isValid = false
        } else {
            layoutTitle.error = null
        }

        if (etVehicle.text.isNullOrEmpty()) {
            layoutVehicle.error = "Vehicle Identifier is required"
            isValid = false
        } else {
            layoutVehicle.error = null
        }

        if (etPrice.text.isNullOrEmpty()) {
            layoutPrice.error = "Price is required"
            isValid = false
        } else {
            try {
                etPrice.text.toString().toDouble()
                layoutPrice.error = null
            } catch (e: NumberFormatException) {
                layoutPrice.error = "Invalid price"
                isValid = false
            }
        }

        if (actvStatus.text.isNullOrEmpty()) {
            (actvStatus.parent.parent as? TextInputLayout)?.error = "Status is required"
            isValid = false
        } else {
            (actvStatus.parent.parent as? TextInputLayout)?.error = null
        }

        return isValid
    }

    private fun saveEvent() {
        val id = etId.text.toString()
        val title = etTitle.text.toString()
        val vehicle = etVehicle.text.toString()
        val description = etDescription.text.toString()
        val price = etPrice.text.toString().toDouble()
        val category = if (toggleCategory.checkedButtonId == R.id.btn_service) "SERVICE" else "DOCUMENT"
        val status = actvStatus.text.toString()
        val dueDate = calendar.time

        val newEvent = MaintenanceEvent(
            id = id,
            title = title,
            vehicleIdentifier = vehicle,
            description = description,
            category = category,
            dueDate = dueDate,
            status = status,
            price = price
        )

        viewModel.addEvent(newEvent)
        Toast.makeText(context, "Event Saved", Toast.LENGTH_SHORT).show()

        findNavController().popBackStack()
    }
}
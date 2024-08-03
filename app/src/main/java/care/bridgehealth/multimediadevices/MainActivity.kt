package care.bridgehealth.multimediadevices

import android.content.Context
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hoho.android.usbserial.driver.UsbSerialDriver
import com.hoho.android.usbserial.driver.UsbSerialProber

class MainActivity : AppCompatActivity() {

    private lateinit var usbManager: UsbManager
    private lateinit var usbDeviceSpinner: Spinner
    private var usbDevices: List<UsbSerialDriver> = listOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        usbManager = getSystemService(Context.USB_SERVICE) as UsbManager
        usbDeviceSpinner = findViewById(R.id.usbDeviceSpinner)

        listUsbDevices()
        setupSpinner()

    }

    private fun listUsbDevices() {
        val availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager)
        usbDevices = availableDrivers

        if (availableDrivers.isEmpty()) {
            Log.e("MainActivity", "No USB devices found")
            return
        }

        val deviceNames = availableDrivers.map { it.device.deviceName }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, deviceNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        usbDeviceSpinner.adapter = adapter
    }

    private fun setupSpinner() {
        usbDeviceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedDevice = usbDevices[position].device
                useUsbDevice(selectedDevice)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
    }

    private fun useUsbDevice(device: UsbDevice) {
        Log.i("MainActivity", "Selected USB Device:")
        Log.i("MainActivity", "Device Name: ${device.deviceName}")
        Log.i("MainActivity", "Vendor ID: ${device.vendorId}")
        Log.i("MainActivity", "Product ID: ${device.productId}")
        Log.i("MainActivity", "Manufacturer Name: ${device.manufacturerName}")
        Log.i("MainActivity", "Product Name: ${device.productName}")
    }
}
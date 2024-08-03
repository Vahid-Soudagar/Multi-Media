package care.bridgehealth.multimediadevices

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hoho.android.usbserial.driver.UsbSerialDriver
import com.hoho.android.usbserial.driver.UsbSerialProber

class MainActivity : AppCompatActivity() {

    private lateinit var usbManager: UsbManager
    private lateinit var usbDeviceList: List<UsbDevice>
    private lateinit var spinner: Spinner
    private lateinit var button: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        usbManager = getSystemService(Context.USB_SERVICE) as UsbManager
        spinner = findViewById(R.id.usbDeviceSpinner)
        button = findViewById(R.id.button)

        val usbPermissionIntent = PendingIntent.getBroadcast(this, 0, Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_IMMUTABLE)
        val filter = IntentFilter(ACTION_USB_PERMISSION)
        registerReceiver(usbReceiver, filter)

        usbDeviceList = getConnectedUsbDevices()
        val deviceNames = usbDeviceList.map { it.deviceName }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, deviceNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        button.setOnClickListener {
            val selectedDeviceName = spinner.selectedItem as String
            Toast.makeText(this, "Selected USB Device: $selectedDeviceName", Toast.LENGTH_SHORT).show()
        }

    }

    private fun getConnectedUsbDevices(): List<UsbDevice> {
        val deviceList = usbManager.deviceList
        return deviceList.values.toList()
    }



    private fun useUsbDevice(device: UsbDevice) {
        Log.i("MainActivity", "Selected USB Device:")
        Log.i("MainActivity", "Device Name: ${device.deviceName}")
        Log.i("MainActivity", "Vendor ID: ${device.vendorId}")
        Log.i("MainActivity", "Product ID: ${device.productId}")
        Log.i("MainActivity", "Manufacturer Name: ${device.manufacturerName}")
        Log.i("MainActivity", "Product Name: ${device.productName}")
    }

    companion object {
        private const val ACTION_USB_PERMISSION = "com.example.USB_PERMISSION"
    }

    private val usbReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == ACTION_USB_PERMISSION) {
                synchronized(this) {
                    val device = intent.getParcelableExtra<UsbDevice>(UsbManager.EXTRA_DEVICE)
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        device?.let {
                            // Permission granted
                        }
                    } else {
                        // Permission denied
                    }
                }
            }
        }
    }

}
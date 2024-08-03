package care.bridgehealth.multimediadevices
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.util.Log

class UsbBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        val usbManager = context?.getSystemService(Context.USB_SERVICE) as UsbManager

        when (action) {
            UsbManager.ACTION_USB_DEVICE_ATTACHED -> {
                val device = intent.getParcelableExtra<UsbDevice>(UsbManager.EXTRA_DEVICE)
                device?.let {
                    logUsbDeviceDetails(it)
                }
            }
            UsbManager.ACTION_USB_DEVICE_DETACHED -> {
                val device = intent.getParcelableExtra<UsbDevice>(UsbManager.EXTRA_DEVICE)
                device?.let {
                    Log.i("UsbBroadcastReceiver", "USB Device Detached: ${it.deviceName}")
                }
            }
        }
    }

    private fun logUsbDeviceDetails(device: UsbDevice) {
        Log.i("UsbBroadcastReceiver", "USB Device Attached:")
        Log.i("UsbBroadcastReceiver", "Device Name: ${device.deviceName}")
        Log.i("UsbBroadcastReceiver", "Manufacturer Name: ${device.manufacturerName}")
        Log.i("UsbBroadcastReceiver", "Product Name: ${device.productName}")
        Log.i("UsbBroadcastReceiver", "Vendor ID: ${device.vendorId}")
        Log.i("UsbBroadcastReceiver", "Product ID: ${device.productId}")
    }
}

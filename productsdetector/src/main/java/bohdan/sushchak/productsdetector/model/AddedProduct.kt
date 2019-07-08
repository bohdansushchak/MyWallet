package bohdan.sushchak.productsdetector.model

import android.os.Parcel
import android.os.Parcelable

class AddedProduct(
    val product: String,
    var count: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(product)
        parcel.writeInt(count)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AddedProduct> {
        override fun createFromParcel(parcel: Parcel): AddedProduct {
            return AddedProduct(parcel)
        }

        override fun newArray(size: Int): Array<AddedProduct?> {
            return arrayOfNulls(size)
        }
    }

}
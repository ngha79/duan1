/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package supermartket.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

public class CloudinaryConfig {
    private static final Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
        "cloud_name", "dlzulba2u",
        "api_key", "231713278731638",
        "api_secret", "P_zyTjSsaWhJXnkt8nwKuZwLDso"
    ));

    public static Cloudinary getInstance() {
        return cloudinary;
    }
}

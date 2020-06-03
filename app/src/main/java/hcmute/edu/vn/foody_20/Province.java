package hcmute.edu.vn.foody_20;

public class Province {

    private int ProvinceId;
    private String ProvinceName;

    public int getProvinceId() {
        return ProvinceId;
    }

    public void setProvinceId(int provinceId) {
        ProvinceId = provinceId;
    }

    public String getProvinceName() {
        return ProvinceName;
    }

    public void setProvinceName(String provinceName) {
        ProvinceName = provinceName;
    }
    public Province(int provinceId, String provinceName) {
        ProvinceId = provinceId;
        ProvinceName = provinceName;
    }



}

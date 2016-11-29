package com.photoselect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.photoselect.adapter.NetPhotoListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 作者：guofeng
 * 日期:2016/11/29
 */

public class NetPhotoActivity extends AppCompatActivity {
    private List<String> data = Arrays.asList(
            "http://cdn2.image.apk.gfan.com/PImages/2016/10/22/ldpi_0_bc8995a1-b6d3-46bf-852d-669c372c8910.png",
            "http://img5.anzhi.com/data2/thumb/201607/19/3548e586a7a277e7e599fd4405668574_58396100.jpg",
            "http://cdn2.image.apk.gfan.com/asdf/PImages/2015/11/30/ldpi_167663_e264a483-715b-4852-9908-a99f80f9027c.jpg",
            "http://cdn2.image.apk.gfan.com/asdf/PImages/2015/9/24/ldpi_258424_21c267c3d-a157-4253-8c4a-f0d9afbb8ece.png",
            "http://cdn2.image.apk.gfan.com/asdf/PImages/2014/11/25/ldpi_1002460_2e08c08dc-394f-4082-ad9a-5bc3508218ef.jpg",
            "http://cdn2.image.apk.gfan.com/PImages/2016/10/22/ldpi_0_bc8995a1-b6d3-46bf-852d-669c372c8910.png",
            "http://cdn2.image.apk.gfan.com/asdf/PImages/2016/07/14/ldpi_544197_a1696469-f313-440d-8402-62b7b5a3066c.png",
            "http://cdn2.image.apk.gfan.com/asdf/PImages/2013/12/4/ldpi_153963_296c8161b-f6ba-4383-aa9f-53d520789983.png",
            "http://cdn2.image.apk.gfan.com/asdf/PImages/2013/5/20/ldpi_141784_28db7fbee-b1b4-4488-8230-13c8a9b53f03.png",
            "http://cdn2.image.apk.gfan.com/PImages/2016/08/29/0_fcf5c6b7-ec74-4612-a2b1-830229033d53.jpg",
            "http://img2.anzhi.com/data2/thumb/201607/19/6008dd79fadffb0cfc45eb0c27caa3bd_67493900.jpg",
            "http://img5.anzhi.com/data2/thumb/201607/19/32f7e6f5f06782f1e33866db0d02e031_50384600.jpg",
            "http://img4.anzhi.com/data2/thumb/201605/16/98ac2e02535430fa1134ecdeb01ce293_69294600.jpg",
            "http://img5.anzhi.com/data2/thumb/201605/16/b3920a0eed23a45cc64813bfb20e0f16_01489700.jpg",
            "http://img5.anzhi.com/data2/thumb/201605/16/374b99622b3d5d0c7e16251651aebee1_11488900.jpg",
            "http://img5.anzhi.com/data2/thumb/201607/26/2042b9a2d6a16b05c119936179dd2b97_97663000.jpg",
            "http://img3.anzhi.com/data2/thumb/201604/01/7849fdeba29595f52f713cdcc3c2aa11_92009400.jpg",
            "http://img2.anzhi.com/data2/thumb/201604/01/c9cf660f1fef04f8f6cb87e8aa5622fb_05608900.jpg",
            "http://img1.anzhi.com/data2/thumb/201610/20/13b8ddfddcf44ab5fe83122d2981a92f_53381500.jpg",
            "http://img1.anzhi.com/data2/thumb/201609/23/696b6ec7abd7ab02aaef05f80e758e53_06520500.jpg",
            "http://img4.anzhi.com/data2/thumb/201609/23/c3cea6ce60987267f270dfb1d2596c9f_33719700.jpg",
            "http://img4.anzhi.com/data2/thumb/201610/20/3aaf8c25c90f72ad7288fee2d54b071b_95279600.jpg",
            "http://img1.anzhi.com/data2/thumb/201605/27/97e122ee8306054267d2b9aa8608e0e6_50216800.jpg",
            "http://img3.anzhi.com/data2/thumb/201605/27/7008e5a0bf9880d04dd6d3f18e431f21_61616800.jpg",
            "http://img2.anzhi.com/data2/thumb/201605/27/c1234dbc93bc99df0acfe41e6c06c961_78215900.jpg",
            "http://img2.anzhi.com/data2/thumb/201605/27/09516d4cd497dc85d2733392e85c4ae3_97814900.jpg",
            "http://img4.anzhi.com/data2/thumb/201610/20/3aaf8c25c90f72ad7288fee2d54b071b_95279600.jpg",
            "http://img4.anzhi.com/data2/thumb/201610/20/3aaf8c25c90f72ad7288fee2d54b071b_95279600.jpg",
            "http://img1.anzhi.com/data2/thumb/201605/27/3987b82193cf4b84843b4c6f716407ef_89015600.jpg",
            "http://img1.anzhi.com/data2/thumb/201607/01/abd442841ff287e5e2ed4f6dc578bd74_90614300.jpg",
            "http://img4.anzhi.com/data2/thumb/201607/01/dd90e8c33b79ee61015c1be673fd0f37_93814100.jpg",
            "http://img4.anzhi.com/data2/thumb/201607/01/43c9badc3f0d54e25cc379b4adaff503_96917300.jpg",
            "http://img2.anzhi.com/data2/thumb/201403/29/com.example.chinesechess_99344200.jpg",
            "http://img5.anzhi.com/data1/thumb/201401/07/com.example.chinesechess_12315000.jpg",
            "http://img3.anzhi.com/data1/thumb/201401/07/com.example.chinesechess_13978500.jpg",
            "http://img4.anzhi.com/data1/thumb/201401/07/com.example.chinesechess_15511000.jpg",
            "http://img5.anzhi.com/data1/thumb/201401/20/com.gameloft.android.ANMP.GloftA8CN.anzhi_86639000.jpg",
            "http://img4.anzhi.com/data3/thumb/201507/22/41c795ae8b2dfb6af7ef485917df2ba0_61076900.jpg",
            "http://img1.anzhi.com/data3/thumb/201507/22/7811e4acca92426cb3bb45509d86ed16_63776700.jpg",
            "http://img3.anzhi.com/data3/thumb/201507/22/1befa548b8ff550229d61889fddba93a_69176400.jpg",
            "http://img3.anzhi.com/thumb/201304/23/air.HaydayForAd_58819400_0.jpg",
            "http://img4.anzhi.com/thumb/201304/23/air.HaydayForAd_70923600_2.jpg",
            "http://img5.anzhi.com/thumb/201304/23/air.HaydayForAd_77629400_3.jpg",
            "http://img3.anzhi.com/data2/thumb/201610/24/d6781e5de742f9dad08494d3e69352ce_98739100.jpg",
            "http://img1.anzhi.com/data2/thumb/201610/24/c66b2757e194976aefaa12c4aaa5a159_52336900.jpg",
            "http://img3.anzhi.com/data2/thumb/201609/28/b1a548f72d328dd88ee58c83b62fff7d_27283500.jpg",
            "http://img5.anzhi.com/data2/thumb/201609/28/174a1f0e3b442827f66b4370c920b62a_14484300.jpg",
            "http://img1.anzhi.com/data2/thumb/201608/10/3f2740cd5a610350ff3c5bbb0213ff23_29729000.jpg",
            "http://img3.anzhi.com/data2/thumb/201608/10/890f6eacc7f885730491a019aacbb30e_17932300.jpg",
            "http://img4.anzhi.com/data2/thumb/201608/10/d005a5ab290ccec7090378b9b5ee3209_04832900.jpg",
            "http://img2.anzhi.com/data2/thumb/201608/10/56362246efb849028338616cfa3569ac_51831400.jpg",
            "http://img2.anzhi.com/data2/thumb/201609/20/2c5a46f1eb07161607eaa0d05c603681_51413500.jpg",
            "http://img2.anzhi.com/data2/thumb/201609/20/c90df57c6bfc52773bec14eb9c2d93d6_71612100.jpg",
            "http://img3.anzhi.com/data2/thumb/201609/23/2edae645148860e76a0c5eb780649f9c_85820100.jpg");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_photo);

        final ArrayList list = new ArrayList();
        list.addAll(data);

        ListView listview = (ListView) findViewById(R.id.listview);
        NetPhotoListAdapter adapter = new NetPhotoListAdapter(this);
        adapter.setData(list);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PhotoDetailActivity.goToPhotoDetail(NetPhotoActivity.this, i, list, 0);
            }
        });
    }

}

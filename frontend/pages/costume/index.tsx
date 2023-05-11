// 유저별 코스튬 페이지
import { useRouter } from "next/router";
import { Fragment } from "react";
import * as page from "@/components/layout/PageWrapper/style/PageWrapper";
import BottomNavBar from "@/components/layout/BottomNavBar/BottomNavBar";
import CostumePage from "@/components/page/CostumePage/CostumePage";
import { GetServerSideProps } from "next";
import { getAllItemList } from "../api/main";

// Costumepage
const CostumeFunc = (props: any) => {
  // const router = useRouter();

  return (
    <Fragment>
      <page.PageWrapper>
        <CostumePage
          headItemList={props.HEAD_ITEMS}
          bodyItemList={props.BODY_ITEMS}
        />
      </page.PageWrapper>
      <BottomNavBar />
    </Fragment>
  );
};

export const getServerSideProps: GetServerSideProps =
  async (context) => {
    // const userId = "9302629d-ae6a-43b6-a965-996d5429783c";

    // HEAD 코스튬 목록 불러오기
    const resHEAD = await getAllItemList("HEAD");
    // BODY 코스튬 목록 불러오기
    const resBODY = await getAllItemList("BODY");
    // // EFFECT 코스튬 목록 불러오기
    const resEFFECT = await getAllItemList("EFFECT");

    const HEAD_ITEMS = resHEAD.data.content;
    const BODY_ITEMS = resBODY.data.content;
    const EFFECT_ITEMS = resEFFECT.data.content;

    return {
      props: {
        HEAD_ITEMS,
        BODY_ITEMS,
        EFFECT_ITEMS,
      },
    };
  };

export default CostumePage;

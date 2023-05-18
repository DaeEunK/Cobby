// 유저별 코스튬 페이지
import { Fragment, useState, useEffect, useLayoutEffect } from "react";
import * as page from "@/components/layout/PageWrapper/style/PageWrapper";
import BottomNavBar from "@/components/layout/BottomNavBar/BottomNavBar";
import TextBox from "@/components/common/TextBox/TextBox";
import Inventory from "./CostumeComponents/Inventory";
import * as style from "./CostumeComponents/style/CostumePage";
import Cobby from "@/components/common/Cobby/Cobby";
import { getAvatarInfo, patchAvatarInfo } from "@/pages/api/main";
import cookie from "react-cookies";

interface Props {
  HEAD_ITEMS: any;
  BODY_ITEMS: any;
  EFFECT_ITEMS: any;
  cobbyOutfits: any;
}

// CostumePage
const CostumePage = (props: Props) => {
  const [outfits, setOutfits]: any = useState(props.cobbyOutfits);

  const [cobby, setCobby]: any = useState({
    baseCobby: "/Character/Cobby.gif" + "?" + Date.now(),
    head: null,
    body: null,
    effect: null,
  });

  const handleInventoryItem = (itemInfo: any) => {
    // 나의 Cobby 의 outfits 업데이트해주기
    setOutfits((prevOutfits: any) => {
      let updatedOutfits = { ...prevOutfits };

      if (itemInfo.category === "HEAD") {
        updatedOutfits.head = itemInfo;

        setCobby((state: any) => {
          const nextState = {
            baseCobby: newImgReq("/Character/Cobby.gif"),
            head: newImgReq(updatedOutfits.head.gifUrl),
            body: state.body ? newImgReq(updatedOutfits.body.gifUrl) : null,
            effect: state.effect
              ? newImgReq(updatedOutfits.effect.gifUrl)
              : null,
          };

          return nextState;
        });
      } else if (itemInfo.category === "BODY") {
        updatedOutfits.body = itemInfo;

        setCobby((state: any) => {
          const nextState = {
            baseCobby: newImgReq("/Character/Cobby.gif"),
            head: state.head ? newImgReq(updatedOutfits.head.gifUrl) : null,
            body: newImgReq(updatedOutfits.body.gifUrl),
            effect: state.effect
              ? newImgReq(updatedOutfits.effect.gifUrl)
              : null,
          };

          return nextState;
        });
      } else if (itemInfo.category === "EFFECT") {
        updatedOutfits.effect = itemInfo;

        setCobby((state: any) => {
          const nextState = {
            baseCobby: newImgReq("/Character/Cobby.gif"),
            head: state.head ? newImgReq(updatedOutfits.head.gifUrl) : null,
            body: state.body ? newImgReq(updatedOutfits.body.gifUrl) : null,
            effect: newImgReq(updatedOutfits.effect.gifUrl),
          };

          return nextState;
        });
      }

      // 코비 정보 수정
      const token = cookie.load("Authorization");

      const data = {
        head: updatedOutfits.head.costumeId,
        body: updatedOutfits.body.costumeId,
        effect: updatedOutfits.effect.costumeId,
      };

      patchAvatarInfo(token, data).then((res) => {});

      return updatedOutfits;
    });
  };

  const cacheImages = async (srcArray: any) => {
    const promiseArray = srcArray.map((src: string) => {
      if (src) {
        return new Promise((resolve: any, reject) => {
          const img = new Image();
          img.onload = () => {
            resolve();
          };
          img.onerror = () => {
            reject();
          };
          img.src = src;
        });
      }
    });

    await Promise.all(promiseArray);
  };

  const useCacheImg = (cobby: any) => {
    const [isLoading, setIsLoading] = useState(true);

    useLayoutEffect(() => {
      cacheImages(Object.values(cobby))
        .then(() => {
          setIsLoading(false);
        })
        .catch((err) => {
          setIsLoading(true);
        });
    }, [cobby]);

    return isLoading;
  };

  const isLoading = useCacheImg(cobby);

  const newImgReq = (src: string) => {
    return src + "?" + Date.now();
  };

  return (
    <Fragment>
      <page.PageWrapper>
        <style.CostumePageTextWrapper>
          <TextBox size={50} content={"COSTUME"} />
        </style.CostumePageTextWrapper>
        <style.CostumedCobby>
          <Cobby outfits={outfits} isLoading={isLoading} cobby={cobby} />
        </style.CostumedCobby>
        <Inventory
          headItemList={props.HEAD_ITEMS}
          bodyItemList={props.BODY_ITEMS}
          effectItemList={props.EFFECT_ITEMS}
          outfits={outfits}
          onItemClick={handleInventoryItem}
        />
      </page.PageWrapper>
      <BottomNavBar />
    </Fragment>
  );
};

export default CostumePage;

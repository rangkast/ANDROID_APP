package com.toadstudio.first.toadproject.RecycleView;

import jp.satorufujiwara.binder.Section;

public enum DemoSectionType implements Section {
  ITEM {
      @Override
      public int position() {
          return 0;
      }
  }
}

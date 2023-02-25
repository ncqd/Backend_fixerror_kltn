package com.iuh.backendkltn32.service;

import com.iuh.backendkltn32.entity.HocKy;

import java.util.List;

public interface HocKyService extends AbstractService<HocKy>{

    List<HocKy> layTatCaHocKy();
}

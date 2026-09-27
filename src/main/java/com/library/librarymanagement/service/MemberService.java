package com.library.librarymanagement.service;

import com.library.librarymanagement.model.Member;

import java.util.List;

public interface MemberService {
    Member createMember(Member member);
    List<Member> getAllMembers();
    Member getMemberById(Long id);
    Member updateMember(Long id, Member member);
    void deleteMember(Long id);
}

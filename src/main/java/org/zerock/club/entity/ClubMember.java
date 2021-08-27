package org.zerock.club.entity;

import lombok.*;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ClubMember extends BaseEntity{

    @Id
    private String email;

    private String password;

    private String name;

    private boolean fromSocial;

    /**
     * ClubMember의 여러개의 권한을 가지려면(Set) 객체의 일부로만 사용(Builder.Default)
     * 그리고 필요할때만 가져오려면 Lazy 사용, 바로 가져올경우는 eager
     */
    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<ClubMemberRole> roleSet = new HashSet<>();

    public void addMemberRole(ClubMemberRole clubMemberRole) {
        roleSet.add(clubMemberRole);
    }
}